<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'
import { useToast } from '@/composables/useToast'
import AuthLayout from '@/components/auth/AuthLayout.vue'

const router = useRouter()
const { showToast } = useToast()

const showPassword = ref(false)
const showConfirmPassword = ref(false)

const form = ref({
  name: '',
  username: '',
  password: '',
  confirmPassword: '',
})

const isLoading = ref(false)

function getErrorMessage(err, fallbackMessage) {
  return err.response?.data?.message || fallbackMessage
}

async function registerUser() {
  if (form.value.password !== form.value.confirmPassword) {
    showToast('Passwords do not match', 'error')
    return
  }

  isLoading.value = true

  if (!isValidPassword(form.value.password)) {
    showToast('Password must contain at least 8 characters, one letter and one number', 'error')

    return
  }

  try {
    await api.post('/api/register', {
      name: form.value.name,
      username: form.value.username,
      password: form.value.password,
    })

    showToast('Account created successfully', 'success')

    router.push('/login')
  } catch (err) {
    console.error(err)

    showToast(getErrorMessage(err, 'Could not create account'), 'error')
  } finally {
    isLoading.value = false
  }
}

function isValidPassword(password) {
  const regex = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/

  return regex.test(password)
}
</script>

<template>
  <AuthLayout title="Create Account" subtitle="Join the tournament experience">
    <form class="register-form">
      <div class="form-group">
        <label for="name">Name</label>

        <input id="name" v-model="form.name" type="text" placeholder="Your name" />
      </div>

      <div class="form-group">
        <label for="username">Username</label>

        <input id="username" v-model="form.username" type="text" placeholder="Choose a username" />
      </div>

      <div class="form-group">
        <label for="password">Password</label>

        <div class="password-wrapper">
          <input
            id="password"
            v-model="form.password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="Choose a password"
          />

          <button
            type="button"
            class="password-toggle"
            @click="showPassword = !showPassword"
            :aria-label="showPassword ? 'Hide password' : 'Show password'"
            :title="showPassword ? 'Hide password' : 'Show password'"
          >
            <i :class="showPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'"></i>
          </button>
        </div>
      </div>

      <div class="form-group">
        <label for="confirmPassword">Confirm password</label>

        <div class="password-wrapper">
          <input
            id="confirmPassword"
            v-model="form.confirmPassword"
            :type="showConfirmPassword ? 'text' : 'password'"
            placeholder="Repeat password"
          />

          <button
            type="button"
            class="password-toggle"
            @click="showConfirmPassword = !showConfirmPassword"
            :aria-label="showConfirmPassword ? 'Hide password' : 'Show password'"
            :title="showConfirmPassword ? 'Hide password' : 'Show password'"
          >
            <i :class="showConfirmPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'"></i>
          </button>
        </div>
      </div>

      <p class="password-hint">Minimum 8 characters, one letter and one number.</p>

      <button type="button" class="register-button" :disabled="isLoading" @click="registerUser">
        {{ isLoading ? 'Creating...' : 'Create account' }}
      </button>
    </form>

    <button type="button" class="back-login" @click="router.push('/login')">Back to login</button>
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

.password-wrapper {
  position: relative;
}

.password-wrapper input {
  padding-right: 48px;
}

.password-toggle {
  position: absolute;
  top: 50%;
  right: 14px;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  background: transparent;
  color: var(--primary);
  font-size: 1rem;
  cursor: pointer;
}

.password-hint {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.82rem;
  line-height: 1.4;
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

.back-login {
  display: block;
  margin: 12px auto;
  border: none;
  background: none;
  color: var(--primary);
  font-weight: 800;
  cursor: pointer;
}

.back-login:hover {
  color: var(--primary-light);
}
</style>
