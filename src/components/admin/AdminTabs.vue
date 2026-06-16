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

    <h3 class="panel-title">
      Match Control Panel
    </h3>

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
    </div>

  </div>
</template>

<style scoped>
.admin-tabs-container {
  margin-bottom: 24px;
}

.panel-title {
  margin: 0 0 16px 0;

  font-size: 1.4rem;
  font-weight: 800;

  color: var(--text-primary);
}

.admin-tabs button {
  display: flex;
  align-items: center;
  gap: 8px;
}

.admin-tabs {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  border-bottom: 1px solid var(--border);
  padding-bottom: 8px;
}

.admin-tabs button {
  display: flex;
  align-items: center;
  gap: 8px;
  background: none;
  border: none;
  color: var(--text-secondary);
  font-weight: 800;
  white-space: nowrap;
  padding: 10px 0;
  position: relative;
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

</style>
