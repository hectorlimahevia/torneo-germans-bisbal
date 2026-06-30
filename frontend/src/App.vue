<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { isAuthenticated, currentUser, isAdmin, logout } from '@/auth/auth'
import AiChatWidget from '@/components/AiChatWidget.vue'
import AppFooter from '@/components/AppFooter.vue'
import ToastContainer from '@/components/ToastContainer.vue'

const isMenuOpen = ref(false)
const router = useRouter()

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value
}

function closeMenu() {
  isMenuOpen.value = false
}

function handleLogout() {
  logout()
  closeMenu()

  router.push('/')
}
</script>

<template>
  <div class="app">
  <header class="app-header">
    <button class="menu-button" :class="{ open: isMenuOpen }" type="button" @click="toggleMenu">
      {{ isMenuOpen ? '✕' : '☰' }}
    </button>

    <div v-if="isAuthenticated" class="user-badge">
      <i :class="isAdmin ? 'fa-solid fa-shield-halved' : 'fa-solid fa-users'"></i>

      <span>{{ currentUser }}</span>
    </div>
  </header>

  <nav v-if="isMenuOpen" class="mobile-menu">
    <RouterLink to="/" @click="closeMenu">Home</RouterLink>
    <RouterLink to="/standings" @click="closeMenu">Standings</RouterLink>
    <RouterLink to="/matches" @click="closeMenu">Matches</RouterLink>
    <RouterLink to="/teams" @click="closeMenu">Teams</RouterLink>
    <RouterLink to="/rules" @click="closeMenu">Rules</RouterLink>

    <RouterLink v-if="isAdmin" to="/admin" @click="closeMenu"> Admin </RouterLink>

    <RouterLink v-if="!isAuthenticated" to="/login" @click="closeMenu"> Login </RouterLink>

    <button v-if="isAuthenticated" type="button" class="logout-link" @click="handleLogout">
      Logout
    </button>
  </nav>

  <main class="app-main">
    <RouterView v-slot="{ Component, route }">
      <component :is="Component" :class="{ 'page-container': route.name !== 'login' }" />
    </RouterView>
  </main>

  <AiChatWidget v-if="isAuthenticated" />

  <AppFooter  />
  <ToastContainer />
  </div>
</template>

<style scoped>
.app {
  margin-top: 3rem;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
}

.page-container {
  padding-inline: 16px;
  padding-bottom: 10px;
}

.app-header {
  position: fixed;
  top: 18px;
  left: 18px;
  z-index: 100;
}

.menu-button {
  width: 48px;
  height: 48px;
  background: white;
  border: 1px solid var(--border);
  border-radius: 32px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  color: var(--primary);
  font-size: 30px;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.menu-button.open {
  background: var(--primary);
  color: white;
}
.mobile-menu {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 90;
  width: 270px;
  height: 100vh;
  background: var(--primary);
  display: flex;
  flex-direction: column;
  padding-top: 90px;
  box-shadow: var(--shadow);
}

.mobile-menu a,
.logout-link {
  padding: 16px 28px;
  color: white;
  background: transparent;
  text-decoration: none;
  font-weight: 500;
  font-size: 16px;
  border: none;
  text-align: left;
  cursor: pointer;
}

.mobile-menu a:hover,
.logout-link:hover {
  background: rgba(255, 255, 255, 0.12);
}



.user-badge {
  position: fixed;
  top: 18px;
  right: 18px;
  z-index: 100;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  background: var(--primary-light);
  border: 1px solid var(--border);
  border-radius: 999px;
  box-shadow: var(--shadow);
  color: #fff;
  font-size: 14px;
  font-weight: 700;
}

.user-badge i {
  font-size: 0.95rem;
}
</style>
