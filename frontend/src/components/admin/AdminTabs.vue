<script setup>
const props = defineProps({
  selectedTab: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(['tab-selected'])
</script>

<template>
  <div class="admin-tabs-container">
    <h3 class="panel-title">Match Control Panel</h3>

    <div class="admin-tabs">
      <button
        type="button"
        :class="{ active: props.selectedTab === 'create' }"
        @click="emit('tab-selected', 'create')"
      >
        <i class="fa-solid fa-calendar-days"></i>
        Schedule
      </button>

      <button
        type="button"
        :class="{ active: props.selectedTab === 'update' }"
        @click="emit('tab-selected', 'update')"
      >
        <i class="fa-solid fa-trophy"></i>
        Update
      </button>

      <button
        type="button"
        :class="{ active: props.selectedTab === 'field' }"
        @click="emit('tab-selected', 'field')"
      >
        <i class="fa-solid fa-location-dot"></i>
        Create Field
      </button>

      <button
        type="button"
        :class="{ active: props.selectedTab === 'users' }"
        @click="emit('tab-selected', 'users')"
      >
        <i class="fa-solid fa-users-gear"></i>
        Users
      </button>
    </div>
  </div>
</template>

<style scoped>
.admin-tabs-container {
  margin-top: 24px;
  margin-bottom: 24px;
}

.panel-title {
  margin: 0 0 16px 0;

  color: var(--text-primary);

  font-size: 1.4rem;
  font-weight: 800;
}

.admin-tabs {
  display: grid;
  grid-template-columns: repeat(4, auto);
  align-items: center;
  gap: 20px;

  border-bottom: 1px solid var(--border);

  padding-bottom: 8px;
}

.admin-tabs button {
  position: relative;

  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  padding: 10px 0;

  background: none;
  border: none;

  color: var(--text-secondary);

  font-weight: 800;
  white-space: nowrap;

  cursor: pointer;
}

.admin-tabs button i {
  font-size: 1.4rem;
}

.admin-tabs button.active {
  color: var(--primary);
}

.admin-tabs button.active::after {
  content: '';

  position: absolute;
  left: 0;
  right: 0;
  bottom: -9px;

  height: 4px;

  background: var(--primary);
  border-radius: 999px;
}

@media (max-width: 768px) {
  .admin-tabs {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;

    border-bottom: none;
  }

  .admin-tabs button {
    min-height: 72px;

    flex-direction: column;
    gap: 6px;

    padding: 12px 8px;

    background: var(--card);

    border: 1px solid var(--border);
    border-radius: 14px;

    box-shadow: var(--shadow);

    text-align: center;
  }

  .admin-tabs button i {
    font-size: 1.5rem;
  }

  .admin-tabs button.active {
    border-color: var(--primary);
    background: rgba(58, 157, 196, 0.08);
  }

  .admin-tabs button.active::after {
    display: none;
  }
}
</style>
