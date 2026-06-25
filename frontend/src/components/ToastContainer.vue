<script setup>
import { useToast } from '@/composables/useToast'

const { toasts, removeToast } = useToast()
</script>

<template>
  <div class="toast-container">
    <div v-for="toast in toasts" :key="toast.id" class="toast" :class="toast.type">
      <span>
        {{ toast.type === 'error' ? '❌' : '✅' }}
      </span>

      <p>{{ toast.message }}</p>

      <button type="button" @click="removeToast(toast.id)">×</button>
    </div>
  </div>
</template>

<style scoped>
.toast-container {
  position: fixed;
  top: 80px;
  right: 16px;
  z-index: 200;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.toast {
  min-width: 260px;
  max-width: 340px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 14px;
  background: white;
  border-radius: 12px;
  box-shadow: var(--shadow);
  border-left: 5px solid var(--primary);
}

.toast.error {
  border-left-color: #dc2626;
}

.toast p {
  margin: 0;
  flex: 1;
  font-size: 14px;
}

.toast button {
  border: none;
  background: transparent;
  font-size: 18px;
  cursor: pointer;
}
</style>
