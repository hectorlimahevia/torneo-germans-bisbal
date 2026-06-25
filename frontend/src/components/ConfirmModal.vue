<script setup>
const props = defineProps({
  title: {
    type: String,
    required: true,
  },

  message: {
    type: String,
    required: true,
  },

  confirmText: {
    type: String,
    default: 'Confirm',
  },

  cancelText: {
    type: String,
    default: 'Cancel',
  },

  danger: {
    type: Boolean,
    default: false,
  },

  isLoading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['confirm', 'cancel'])
</script>

<template>
  <div class="modal-backdrop">
    <div class="confirm-modal">
      <h3>{{ props.title }}</h3>

      <p>{{ props.message }}</p>

      <div class="modal-actions">
<button
  type="button"
  class="cancel-button"
  :disabled="props.isLoading"
  @click="emit('cancel')"
>
  {{ props.cancelText }}
</button>

        <button
          type="button"
          class="confirm-button"
          :class="{ danger: props.danger }"
          :disabled="props.isLoading"
          @click="emit('confirm')"
        >
          {{ props.isLoading ? 'Deleting...' : props.confirmText }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  z-index: 300;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: rgba(0, 0, 0, 0.45);
}

.confirm-modal {
  width: 100%;
  max-width: 360px;
  background: white;
  border-radius: 18px;
  padding: 22px;
  box-shadow: 0 20px 40px rgba(69, 68, 75, 0.25);
  animation: modal-in 0.2s ease;
}

.confirm-modal h3 {
  margin: 0 0 10px 0;
  color: var(--primary);
  font-size: 1.25rem;
}

.confirm-modal p {
  margin: 0;
  color: var(--text-secondary);
  line-height: 1.45;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 22px;
}

.cancel-button,
.confirm-button {
  border: none;
  border-radius: 999px;
  padding: 10px 16px;
  font-weight: 800;
  cursor: pointer;
  transition:
    transform 0.2s ease,
    background 0.2s ease;
}

.cancel-button {
  background: #eef2f7;
  color: var(--text-primary);
}

.confirm-button {
  background: var(--primary);
  color: white;
}

.confirm-button.danger {
  background: #dc2626;
}

.cancel-button:hover,
.confirm-button:hover {
  transform: translateY(-2px);
}

.confirm-button.danger:hover {
  background: #b91c1c;
}

@keyframes modal-in {
  from {
    opacity: 0;
    transform: translateY(8px) scale(0.96);
  }

  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}
</style>
