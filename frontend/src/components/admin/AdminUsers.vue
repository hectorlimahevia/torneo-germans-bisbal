<script setup>
import { computed, ref } from 'vue'
import AdminUserCard from './AdminUserCard.vue'

const props = defineProps({
  users: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['make-admin', 'remove-admin'])

const searchTerm = ref('')

const filteredUsers = computed(() => {
  const term = searchTerm.value.toLowerCase().trim()

  if (!term) {
    return props.users
  }

  return props.users.filter((user) => {
    return user.name.toLowerCase().includes(term) || user.username.toLowerCase().includes(term)
  })
})

const adminUsers = computed(() => {
  return filteredUsers.value.filter((user) => isAdmin(user))
})

const regularUsers = computed(() => {
  return filteredUsers.value.filter((user) => !isAdmin(user))
})

function isAdmin(user) {
  return user.roles.includes('ROLE_ADMIN')
}
</script>

<template>
  <section class="users-card">
    <div class="users-header">
      <h3>User Management</h3>
      <p>Registered users and assigned roles.</p>

      <div class="user-search">
        <i class="fa-solid fa-magnifying-glass"></i>

        <input v-model="searchTerm" type="text" placeholder="Search by name or username..." />
      </div>
    </div>

    <div class="users-list">
      <div class="user-section">
        <h4>
          <i class="fa-solid fa-shield-halved"></i>
          Admins ({{ adminUsers.length }})
        </h4>

        <AdminUserCard
          v-for="user in adminUsers"
          :key="user.id"
          :user="user"
          :is-admin-card="true"
          @remove-admin="emit('remove-admin', $event)"
        />

        <p v-if="adminUsers.length === 0" class="empty-message">No admins found.</p>
      </div>

      <div class="user-section">
        <h4>
          <i class="fa-solid fa-users"></i>
          Users ({{ regularUsers.length }})
        </h4>

        <AdminUserCard
          v-for="user in regularUsers"
          :key="user.id"
          :user="user"
          @make-admin="emit('make-admin', $event)"
        />

        <p v-if="regularUsers.length === 0" class="empty-message">No users found.</p>
      </div>
    </div>
  </section>
</template>

<style scoped>
.users-card {
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: var(--card);
}

.users-header h3 {
  margin: 0;
  color: var(--primary);
}

.users-header p {
  margin: 6px 0 0;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.users-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.user-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 16px;
}

.user-search {
  display: flex;
  margin-top: 12px;
  align-items: center;
  gap: 10px;
  padding: 12px 14px;
  background: white;
  border: 1px solid var(--border);
  border-radius: 999px;
  box-shadow: var(--shadow-sm);
}

.user-search i {
  color: var(--primary);
  font-size: 1rem;
}

.user-search input {
  width: 100%;
  border: none;
  outline: none;
  font-size: 0.95rem;
  font-family: inherit;
  background: transparent;
}

.user-section h4 {
  display: flex;
  align-items: center;
  gap: 8px;

  color: var(--primary);
  font-size: 1.05rem;
  font-weight: 800;
}

</style>
