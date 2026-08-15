package com.ironhack.torneo_germans_bisbal_api.security;

import com.ironhack.torneo_germans_bisbal_api.repository.UserRepository;
import com.ironhack.torneo_germans_bisbal_api.security.filters.CustomAuthenticationFilter;
import com.ironhack.torneo_germans_bisbal_api.security.filters.CustomAuthorizationFilter;
import com.ironhack.torneo_germans_bisbal_api.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManagerBuilder authManagerBuilder;

    // Comma-separated list of origins allowed to call the API, configurable via app.cors.allowed-origins
    @Value("#{'${app.cors.allowed-origins:http://localhost:5173,http://localhost:4173}'.split(',')}")
    private List<String> allowedOrigins;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    @Value("${app.cookie.secure:true}")
    private boolean cookieSecure;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManagerBuilder.getOrBuild(), jwtService, refreshTokenService, userRepository, cookieSecure);

        customAuthenticationFilter.setFilterProcessesUrl("/api/login");


        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/login/**").permitAll()
                        .requestMatchers(POST, "/api/register").permitAll()
                        .requestMatchers(POST, "/api/refresh").permitAll()
                        .requestMatchers(POST, "/api/logout").permitAll()
                        .requestMatchers("/api/greet").permitAll()
                        .requestMatchers("/api/greet/personal").hasAnyAuthority("ROLE_USER")

                        .requestMatchers(GET, "/api/users").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(GET, "/api/clubs/**").permitAll()
                        .requestMatchers(POST, "/api/clubs/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/clubs/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/clubs/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(GET, "/api/fields/**").permitAll()
                        .requestMatchers(POST, "/api/fields/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/fields/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/fields/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(GET, "/api/teams/**").permitAll()
                        .requestMatchers(POST, "/api/teams/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/teams/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/teams/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(GET, "/api/matches/**").permitAll()
                        .requestMatchers(POST, "/api/matches/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/matches/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/matches/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(GET, "/api/standings/**").permitAll()

                        .requestMatchers(POST, "/api/users").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(POST, "/api/roles").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(POST, "/api/roles/add-to-user").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(POST, "/api/roles/remove-from-user").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(GET, "/api/rules/**").permitAll()
                        .requestMatchers(POST, "/api/rules/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/rules/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/rules/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(GET, "/api/ai/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .anyRequest().authenticated());

        http.addFilter(customAuthenticationFilter);

        http.addFilterBefore(new CustomAuthorizationFilter(jwtSecret), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(allowedOrigins);

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
