<script setup>
import { computed } from 'vue'
const props = defineProps({
  status: {
    type: String,
    required: true,
  },
})

const statusClass = computed(() => {
  return props.status.toLowerCase().replace('_', '-')
})
</script>

<template>
  <span :class="['status-badge', statusClass]">
    {{ props.status }}
  </span>
</template>

<style scoped>
.status-badge {
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 800;
  white-space: nowrap;
}

.finished {
  background: #e8f5e9;
  color: #2e7d32;
}

.scheduled {
  background: #e3f2fd;
  color: #1565c0;
}

.in-progress {
  background: #fff3e0;
  color: #ef6c00;
}

.cancelled {
  background: #ffebee;
  color: #c62828;
}
</style>
