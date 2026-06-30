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
  width: 100%;
  max-width: 900px;
  margin: 32px auto;
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background: #f5f7f8;
  border: 1px solid var(--border);
  border-radius: 18px;
  box-shadow: var(--shadow);
}

.users-header h3 {
  margin: 0;
  color: var(--primary);
  font-size: 1.35rem;
  font-weight: 900;
}

.users-header p {
  margin: 6px 0 0;
  color: var(--text-secondary);
  font-size: 0.95rem;
}

.user-search {
  margin-top: 3%;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 14px;
  background: #f8fafc;
  border: 1px solid var(--border);
  border-radius: 999px;
  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.user-search:focus-within {
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(18, 60, 105, 0.15);
}


.user-search i {
  color: var(--primary);
  font-size: 1rem;
}

.user-search input,
.user-search input:focus {
  flex: 1;
  border: none;
  outline: none;
  box-shadow: none;
  background: transparent;
  font-family: inherit;
}

.users-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.user-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.user-section h4 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
  color: var(--primary);
  font-size: 1.05rem;
  font-weight: 900;
}

.empty-message {
  margin: 0;
  padding: 12px;
  color: var(--text-secondary);
  border: 1px dashed var(--border);
  border-radius: 12px;
  background: #f8fafc;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .users-card {
    margin: 24px 0;
    padding: 0;

    background: transparent;
    border: none;
    border-radius: 0;
    box-shadow: none;
  }
}
</style>
