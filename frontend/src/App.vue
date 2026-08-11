<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { isAuthenticated, currentUser, isAdmin, logout } from '@/auth/auth'
import AiChatWidget from '@/components/AiChatWidget.vue'
import AppFooter from '@/components/AppFooter.vue'
import ToastContainer from '@/components/ToastContainer.vue'
import ueslogo from '@/assets/logo_ues.png'

const isMenuOpen = ref(false)
const router = useRouter()

const navLinks = [
  { to: '/', label: 'Home' },
  { to: '/standings', label: 'Standings' },
  { to: '/matches', label: 'Matches' },
  { to: '/teams', label: 'Teams' },
  { to: '/rules', label: 'Rules' },
]

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value
}

function closeMenu() {
  isMenuOpen.value = false
}

async function handleLogout() {
  await logout()
  closeMenu()

  router.push('/')
}
</script>

<template>
  <div class="app">
    <header class="app-header">
      <div class="app-container header-inner">
        <RouterLink to="/" class="brand" @click="closeMenu">
          <img :src="ueslogo" alt="Torneo Germans Bisbal" class="brand-logo" />
          <span class="brand-name">Germans Bisbal</span>
        </RouterLink>

        <nav class="desktop-nav">
          <RouterLink v-for="link in navLinks" :key="link.to" :to="link.to">
            {{ link.label }}
          </RouterLink>

          <RouterLink v-if="isAdmin" to="/admin">Admin</RouterLink>
        </nav>

        <div class="header-actions">
          <RouterLink v-if="!isAuthenticated" to="/login" class="btn btn-accent login-btn">
            Login
          </RouterLink>

          <template v-else>
            <div class="user-badge">
              <i :class="isAdmin ? 'fa-solid fa-shield-halved' : 'fa-solid fa-users'"></i>
              <span>{{ currentUser }}</span>
            </div>

            <button
              type="button"
              class="logout-icon"
              aria-label="Logout"
              title="Logout"
              @click="handleLogout"
            >
              <i class="fa-solid fa-right-from-bracket"></i>
            </button>
          </template>

          <button
            class="menu-button"
            type="button"
            :aria-expanded="isMenuOpen"
            aria-label="Toggle menu"
            @click="toggleMenu"
          >
            <i :class="isMenuOpen ? 'fa-solid fa-xmark' : 'fa-solid fa-bars'"></i>
          </button>
        </div>
      </div>
    </header>

    <div v-if="isMenuOpen" class="menu-backdrop" @click="closeMenu"></div>

    <nav class="mobile-menu" :class="{ open: isMenuOpen }">
      <RouterLink v-for="link in navLinks" :key="link.to" :to="link.to" @click="closeMenu">
        {{ link.label }}
      </RouterLink>

      <RouterLink v-if="isAdmin" to="/admin" @click="closeMenu">Admin</RouterLink>

      <RouterLink v-if="!isAuthenticated" to="/login" @click="closeMenu">Login</RouterLink>

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
        <component
          :is="Component"
          :class="{ 'page-container': !['login', 'register'].includes(route.name) }"
        />
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
  display: flex;
  flex-direction: column;
}

.page-container {
  padding-inline: 16px;
  padding-bottom: 10px;
}

/* ---------- Top navbar ---------- */

.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  height: var(--nav-height);
  background: var(--primary-dark);
  box-shadow: var(--shadow);
}

.header-inner {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
  text-decoration: none;
}

.brand-logo {
  width: 46px;
  height: 46px;
  object-fit: contain;
}

.brand-name {
  display: none;
  font-family: var(--font-heading);
  font-weight: 600;
  font-size: 1.05rem;
  letter-spacing: 0.02em;
}

.desktop-nav {
  display: none;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.login-btn {
  padding: 8px 18px;
  font-size: 0.85rem;
}

.user-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  background: rgba(255, 255, 255, 0.14);
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 999px;
  color: #fff;
  font-size: 13px;
  font-weight: 700;
}

.logout-icon {
  width: 38px;
  height: 38px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 50%;
  background: transparent;
  color: rgba(255, 255, 255, 0.85);
  font-size: 15px;
  cursor: pointer;
}

.logout-icon:hover {
  background: rgba(255, 255, 255, 0.14);
  color: white;
}

.menu-button {
  width: 42px;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  font-size: 18px;
  cursor: pointer;
}

.menu-button:hover {
  background: var(--accent);
  border-color: var(--accent);
  color: var(--primary-dark);
}

.menu-backdrop {
  position: fixed;
  inset: 0;
  z-index: 95;
  background: rgba(8, 27, 48, 0.45);
  backdrop-filter: blur(2px);
}

.mobile-menu {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 98;
  width: 280px;
  max-width: 82vw;
  height: 100vh;
  background: var(--primary-dark);
  display: flex;
  flex-direction: column;
  padding: calc(var(--nav-height) + 12px) 0 0;
  box-shadow: var(--shadow-lg);
  transform: translateX(100%);
  transition: transform 0.28s ease;
}

.mobile-menu.open {
  transform: translateX(0);
}

.mobile-menu a,
.logout-link {
  padding: 16px 28px;
  color: rgba(255, 255, 255, 0.9);
  background: transparent;
  text-decoration: none;
  font-family: var(--font-heading);
  font-weight: 500;
  font-size: 16px;
  border: none;
  text-align: left;
  cursor: pointer;
}

.mobile-menu a.router-link-active {
  color: var(--accent);
}

.mobile-menu a:hover,
.logout-link:hover {
  color: var(--accent);
  background: rgba(255, 255, 255, 0.08);
}

.menu-footer {
  margin-top: auto;
  padding: 22px;
  border-top: 1px solid rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.65);
  text-align: center;
  font-size: 0.8rem;
}

.menu-footer small {
  display: block;
  margin-top: 4px;
  opacity: 0.7;
}

/* ---------- Desktop ---------- */

@media (min-width: 992px) {
  .brand-name {
    display: inline;
  }

  .desktop-nav {
    display: flex;
    align-items: center;
    gap: 28px;
  }

  .desktop-nav a {
    position: relative;
    color: rgba(255, 255, 255, 0.85);
    text-decoration: none;
    font-family: var(--font-heading);
    font-weight: 500;
    font-size: 0.92rem;
    letter-spacing: 0.02em;
    padding: 6px 0;
  }

  .desktop-nav a:hover {
    color: white;
  }

  .desktop-nav a.router-link-exact-active,
  .desktop-nav a.router-link-active {
    color: var(--accent);
  }

  .menu-button {
    display: none;
  }

  .mobile-menu {
    display: none;
  }

  .menu-backdrop {
    display: none;
  }
}
</style>
