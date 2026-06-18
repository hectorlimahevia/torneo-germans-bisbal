<script setup>
import { RouterLink, useRouter } from 'vue-router'
import { login as saveLogin } from '@/auth/auth'
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
  <section class="login-page">
    <div class="login-card">
      <img src="@/assets/logo_ues.png" alt="Torneo Germans Bisbal" class="login-logo" />

      <div class="login-title">
        <span class="login-kicker">TORNEO</span>

        <h1>
          GERMANS<br />
          BISBAL
        </h1>

        <p class="login-slogan">PASSIÓ PEL RUGBY</p>
      </div>

      <form @submit.prevent="login">
        <div>
          <label for="username">Username</label>

          <input id="username" v-model="username" type="text" />
        </div>

        <div>
          <label for="password">Password</label>

          <input id="password" v-model="password" type="password" />
        </div>

        <button type="submit">Login</button>
        <RouterLink to="/" class="guest-link"> Continue as guest </RouterLink>

        <a href="#" class="create-link"> Create new account </a>
      </form>

      <p v-if="message">
        {{ message }}
      </p>
    </div>
  </section>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  background-image: url('@/assets/fondo_login.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  padding: 24px;
}

.login-card {
  width: 100%;
  max-width: 380px;
  padding: 02%;
  text-align: center;
}

.login-logo {
  width: 200px;
  height: 92px;
  object-fit: contain;
  margin-bottom: 18px;
}

.login-kicker {
  display: block;
  color: var(--primary);
  font-weight: 800;
  letter-spacing: 8px;
  font-size: 13px;
  margin-bottom: 6px;
}

.login-title h1 {
  color: var(--primary);
  font-size: 40px;
  line-height: 0.95;
  margin: 0;
  font-weight: 900;
  letter-spacing: 1px;
}

.login-slogan {
  color: var(--primary-light);
  font-size: 13px;
  font-weight: 800;
  letter-spacing: 2px;
  margin-top: 12px;
}

.login-card h1 {
  color: var(--primary);
  text-transform: uppercase;
  line-height: 1.05;
  margin: 0 0 8px;
  font-size: 34px;
  letter-spacing: 1px;
}

.login-card p {
  color: var(--text-secondary);
  margin-bottom: 24px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 14px;
  text-align: left;
}

label {
  font-weight: 700;
  color: var(--primary);
  font-size: 14px;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: 10px;
  font-size: 15px;
}

button {
  width: 50%;
  margin-top: 8px;
  padding: 14px;
  border: none;
  border-radius: 12px;
  background: var(--primary);
  color: white;
  font-weight: 800;
  cursor: pointer;
  display: block;
  margin: auto;
}

.guest-link,
.create-link {
  display: block;
  margin-top: 16px;
  color: var(--primary);
  font-weight: 600;
  text-decoration: none;
}
</style>
