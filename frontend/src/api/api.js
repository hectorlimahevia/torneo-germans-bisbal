import axios from 'axios'
import router from '@/router'

import { login, logout } from '@/auth/auth'

// In production the frontend is served behind a reverse proxy that forwards
// /api requests to the backend on the same origin, so no base URL is needed.
// Locally, Vite's dev server talks directly to the Spring Boot app.
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('access_token')

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

let refreshPromise = null

// If several requests fail at once because the access_token just expired, they must all
// wait on this SAME refresh instead of firing several /api/refresh calls in parallel (each
// one would rotate the refresh token and invalidate the others).
async function refreshAccessToken() {
  if (!refreshPromise) {
    refreshPromise = axios
      .post(`${API_BASE_URL}/api/refresh`, {}, { withCredentials: true })
      .then((res) => res.data.access_token)
      .finally(() => {
        refreshPromise = null
      })
  }

  return refreshPromise
}

api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const { config, response } = error
    const status = response?.status
    const isAuthEndpoint = config?.url?.includes('/api/login') || config?.url?.includes('/api/refresh')

    if ((status === 401 || status === 403) && !isAuthEndpoint && config && !config._retry) {
      config._retry = true

      try {
        const newToken = await refreshAccessToken()
        login(newToken)
        config.headers.Authorization = `Bearer ${newToken}`
        return api(config)
      } catch (refreshError) {
        logout()
        alert('Your session has expired. Please log in again.')
        router.push('/login')
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  },
)

export default api
