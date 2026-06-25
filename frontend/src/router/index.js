import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '@/views/HomeView.vue'
import StandingsView from '../views/StandingsView.vue'
import MatchesView from '../views/MatchesView.vue'
import TeamsView from '../views/TeamsView.vue'
import RulesView from '../views/RulesView.vue'
import LoginView from '../views/LoginView.vue'
import AdminView from '@/views/AdminView.vue'
import { isAdmin } from '@/auth/auth'
import RegisterView from '@/views/RegisterView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/standings',
      name: 'standings',
      component: StandingsView,
    },
    {
      path: '/matches',
      name: 'matches',
      component: MatchesView,
    },
    {
      path: '/teams',
      name: 'teams',
      component: TeamsView,
    },
    {
      path: '/rules',
      name: 'rules',
      component: RulesView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      meta: {
        requiresAdmin: true,
      },
    },
  ],
})
router.beforeEach((to) => {
  if (to.meta.requiresAdmin && !isAdmin.value) {
    return {
      name: 'home',
    }
  }
})
export default router
