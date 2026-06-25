import axios from 'axios'
import router from '@/router'

import { logout } from '@/auth/auth'

const api = axios.create({
  baseURL: 'http://localhost:8080',
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('access_token')

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

let sessionExpired = false

api.interceptors.response.use(
  (response) => response,

  (error) => {
    const status = error.response?.status

    if ((status === 401 || status === 403) && !sessionExpired) {
      sessionExpired = true

      logout()

      alert('Your session has expired. Please log in again.')

      router.push('/login')
    }
    return Promise.reject(error)
  },
)

export default api
