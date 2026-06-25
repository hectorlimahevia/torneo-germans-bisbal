<script setup>
const props = defineProps({
  user: {
    type: Object,
    required: true,
  },

  isAdminCard: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['make-admin', 'remove-admin'])

function formatRole(role) {
  return role.replace('ROLE_', '')
}

function getVisibleRoles(user) {
  if (user.roles.includes('ROLE_ADMIN')) {
    return ['ROLE_ADMIN']
  }

  return ['ROLE_USER']
}
</script>

<template>
  <article class="user-row" :class="{ 'admin-user-row': props.isAdminCard }">
    <div class="user-main">
      <strong>{{ props.user.name }}</strong>
      <span>@{{ props.user.username }}</span>
    </div>

    <div class="role-list">
      <span
        v-for="role in getVisibleRoles(props.user)"
        :key="role"
        class="role-badge"
        :class="{ admin: role === 'ROLE_ADMIN' }"
      >
        {{ formatRole(role) }}
      </span>
    </div>

    <div class="user-actions">
      <button
        v-if="!props.isAdminCard"
        type="button"
        class="admin-button"
        @click="emit('make-admin', props.user)"
      >
        <i class="fa-solid fa-user-shield"></i>
        Make Admin
      </button>

      <button
        v-else
        type="button"
        class="remove-admin-button"
        @click="emit('remove-admin', props.user)"
      >
        <i class="fa-solid fa-user-minus"></i>
        Remove Admin
      </button>
    </div>
  </article>
</template>

<style scoped>
.user-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: 12px;
  background: white;
}

.admin-user-row {
  border-color: rgba(58, 157, 196, 0.4);
  background: rgba(58, 157, 196, 0.05);
}

.user-main {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-main strong {
  color: var(--primary);
}

.user-main span {
  color: var(--text-secondary);
  font-size: 0.85rem;
}

.role-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 6px;
}

.role-badge {
  padding: 5px 8px;
  border-radius: 999px;
  background: #e5e7eb;
  color: var(--text-secondary);
  font-size: 0.75rem;
  font-weight: 800;
}

.role-badge.admin {
  background: var(--primary);
  color: white;
}

.user-actions {
  display: flex;
  justify-content: center;
  margin-top: 12px;
}

.admin-button,
.remove-admin-button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border: none;
  border-radius: 999px;
  color: white;
  font-size: 0.8rem;
  font-weight: 700;
  cursor: pointer;
}

.admin-button {
  background: var(--primary);
}

.remove-admin-button {
  background: #dc2626;
}

.admin-button:hover,
.remove-admin-button:hover {
  opacity: 0.9;
}

@media (max-width: 480px) {
  .user-row {
    display: grid;
    grid-template-columns: 1fr auto;
  }

  .role-list {
    justify-content: flex-start;
  }

   .user-actions {
    grid-column: 1 / -1;
    justify-content: center;
  }

  .admin-button,
  .remove-admin-button {
    width: auto;
    min-width: 180px;
    justify-content: center;
  }
}

</style>
