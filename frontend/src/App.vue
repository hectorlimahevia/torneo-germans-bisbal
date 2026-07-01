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
      <button v-if="!isMenuOpen" class="menu-button" type="button" @click="toggleMenu">☰</button>

      <div v-if="isAuthenticated" class="user-badge">
        <i :class="isAdmin ? 'fa-solid fa-shield-halved' : 'fa-solid fa-users'"></i>

        <span>{{ currentUser }}</span>
      </div>
    </header>

    <nav v-if="isMenuOpen" class="mobile-menu">
      <button class="close-menu" type="button" @click="toggleMenu">✕</button>
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
      <div class="menu-footer">
        <span>Torneo Germans Bisbal</span>
        <small>v1.0</small>
      </div>
    </nav>

    <main class="app-main">
      <RouterView v-slot="{ Component, route }">
        <component :is="Component" :class="{ 'page-container': route.name !== 'login' }" />
      </RouterView>
    </main>

    <AiChatWidget v-if="isAuthenticated" />

    <AppFooter />
    <ToastContainer />
  </div>
</template>

<style scoped>
.app {
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
.close-menu {
  width: 42px;
  height: 42px;
  margin: 22px 0 18px 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  background: transparent;
  color: white;
  font-size: 24px;
  cursor: pointer;
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

.menu-button:hover {
  background: var(--primary);
  color: #fff;
  width: 49px;
  height: 49px;
}

.menu-button.open {
  background: var(--primary);
  color: white;
}

.menu-footer {
  margin-top: auto;
  padding: 22px;
  border-top: 1px solid rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.7);
  text-align: center;
  font-size: 0.8rem;
}

.menu-footer small {
  display: block;
  margin-top: 4px;
  opacity: 0.7;
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
  padding-top: 0;
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
  color: var(--primary-light);
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

/* ---------- Desktop ---------- */

@media (min-width: 992px) {
  .mobile-menu {
    top: 28px;
    left: 18px;
    width: 260px;
    height: auto;
    max-height: calc(100vh - 120px);
    padding: 22px 0;
    border-radius: 22px;
    overflow-y: auto;
    box-shadow: 0 18px 45px rgba(0, 0, 0, 0.28);
  }

  .close-menu {
    position: static;
    align-self: flex-start;
    margin: 0 0 18px 22px;
  }

  .mobile-menu nav {
    margin-top: 8px;
  }

  .mobile-menu a,
  .logout-link {
    padding: 15px 28px;
  }

  .menu-footer {
    margin-top: auto;
  }
}
</style>
