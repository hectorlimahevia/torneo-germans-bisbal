<script setup>
import { RouterLink, useRouter } from 'vue-router'
import { login as saveLogin } from '@/auth/auth'
import AuthLayout from '@/components/auth/AuthLayout.vue'
import { ref } from 'vue'
import api from '@/api/api'

const username = ref('james')
const password = ref('1234')
const message = ref('')
const router = useRouter()

//funcion para el login
async function login() {
  try {
    const response = await api.post('/api/login', {
      username: username.value,
      password: password.value,
    })

    console.log(response.data)
    saveLogin(response.data.access_token)
    message.value = 'Login successful'
    router.push('/')
    message.value = 'Login successful'
  } catch (error) {
    console.error(error)
    message.value = 'Invalid credentials'
  }
}
</script>

<template>
  <AuthLayout title="Create Account" subtitle="Join the tournament experience">
    <form @submit.prevent="login" class="register-form">
      <div class="form-group">
        <label for="username">Username</label>

        <input id="username" v-model="username" type="text" />
      </div>

      <div class="form-group">
        <label for="password">Password</label>

        <input id="password" v-model="password" type="password" />
      </div>

      <button type="submit" class="register-button">Login</button>
      <RouterLink to="/" class="guest-link"> Continue as guest </RouterLink>

      <RouterLink to="/register" class="register-link"> Create new account </RouterLink>
    </form>

    <p v-if="message">
      {{ message }}
    </p>
  </AuthLayout>
</template>

<style scoped>
.register-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  color: var(--primary);
  font-weight: 800;
}

.form-group input,
.password-wrapper input {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: 10px;
  background: white;
  font-family: inherit;
}

.register-button {
  align-self: center;
  width: 50%;
  max-width: 260px;
  padding: 12px 18px;
  border: none;
  border-radius: 999px;
  background: var(--primary);
  color: white;
  font-weight: 800;
  cursor: pointer;
  transition:
    background 0.2s ease,
    transform 0.2s ease;
}

.register-button:hover {
  background: var(--primary-light);
  transform: translateY(-2px);
}

.register-button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
  transform: none;
}

.guest-link,
.register-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: var(--primary);
  font-weight: 600;
  text-decoration: none;
}
.guest-link:hover,
.register-link:hover {
  color: var(--primary-light);
}
</style>
