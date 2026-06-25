import { ref } from 'vue'

const toasts = ref([])

function showToast(message, type = 'success') {
  const id = Date.now()

  toasts.value.push({
    id,
    message,
    type,
  })

  setTimeout(() => {
    removeToast(id)
  }, 3500)
}

function removeToast(id) {
  toasts.value = toasts.value.filter((toast) => toast.id !== id)
}

export function useToast() {
  return {
    toasts,
    showToast,
    removeToast,
  }
}
