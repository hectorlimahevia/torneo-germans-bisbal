import { ref, computed } from 'vue'

const storedToken = ref(localStorage.getItem('access_token'))

export const isAuthenticated = ref(!!storedToken.value)

export function login(token) {
  localStorage.setItem('access_token', token)
  storedToken.value = token
  isAuthenticated.value = true
}

export async function logout() {
  try {
    await fetch((import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080') + '/api/logout', {
      method: 'POST',
      credentials: 'include',
    })
  } catch {
    // if it fails (no connection, etc.) we still clear the local session
  }

  localStorage.removeItem('access_token')
  storedToken.value = null
  isAuthenticated.value = false
}

export const currentUser = computed(() => {
  const token = storedToken.value

  if (!token) {
    return null
  }

  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.sub
  } catch {
    return null
  }
})

export const isAdmin = computed(() => {
  const token = storedToken.value

  if (!token) {
    return false
  }

  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.roles?.includes('ROLE_ADMIN')
  } catch {
    return false
  }
})
