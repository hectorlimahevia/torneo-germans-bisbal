package com.ironhack.torneo_germans_bisbal_api.security.filters;

import com.ironhack.torneo_germans_bisbal_api.dto.LoginRequest;
import com.ironhack.torneo_germans_bisbal_api.repository.UserRepository;
import com.ironhack.torneo_germans_bisbal_api.security.JwtService;
import com.ironhack.torneo_germans_bisbal_api.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import tools.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j // (Simple Logging Facade for Java) offers logging API which is more professional that simply sout
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final boolean cookieSecure;

    /**
     * Constructor for CustomAuthenticationFilter
     *
     * @param authenticationManager
     * @param jwtService
     * @param refreshTokenService
     * @param userRepository
     * @param cookieSecure
     */
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService,
                                       RefreshTokenService refreshTokenService, UserRepository userRepository, boolean cookieSecure) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.cookieSecure = cookieSecure;
    }

    /**
     * Attempts to authenticate the user with given credentials
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Authentication object if successful, otherwise throws an exception
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);

            // Creating an Authentication token with given username and password
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            log.info("Username is: {}", loginRequest.getUsername());
            log.info("Password is: {}", loginRequest.getPassword());

            // Attempting to authenticate the user with the given credentials
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method is called if the user is successfully authenticated
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param chain          FilterChain
     * @param authentication Authentication
     * @throws IOException if there is an Input/Output error
     * @throws ServletException if there is a servlet related error
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // Cast the authentication principal to spring security User object
        User user = (User) authentication.getPrincipal();

        // Generating the access token via the shared JwtService (same logic used by /api/refresh)
        String access_token = jwtService.generateAccessToken(
                user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                request.getRequestURL().toString());

        // Load the application's own User entity (the principal above is Spring Security's User)
        com.ironhack.torneo_germans_bisbal_api.model.User appUser = userRepository.findByUsername(user.getUsername());

        // Issue a refresh token and hand it to the client as an httpOnly cookie
        String refreshToken = refreshTokenService.issue(appUser);

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/api");
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days, in seconds
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        // Creating a map with the generated token
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);

        // Setting the response type to application/json
        response.setContentType(APPLICATION_JSON_VALUE);

        // Writing the token as response
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
