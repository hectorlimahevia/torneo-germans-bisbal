<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },

  subtitle: {
    type: String,
    default: '',
  },

  danger: {
    type: Boolean,
    default: false,
  },
})
</script>

<template>
  <section
    class="admin-form-card"
    :class="{ danger }"
  >
    <header class="admin-form-card-header">
      <h3>{{ title }}</h3>
      <p v-if="subtitle">{{ subtitle }}</p>
    </header>

    <slot />
  </section>
</template>

<style scoped>
.admin-form-card {
  width: 100%;
  max-width: 860px;

  margin: 32px auto;
  padding: 28px;

  background: var(--card);

  border: 1px solid var(--border);
  border-radius: 18px;

  box-shadow: var(--shadow);
}

.admin-form-card.danger {
  border-color: rgba(220, 38, 38, 0.25);
}

.admin-form-card-header {
  margin-bottom: 22px;
}

.admin-form-card-header h3 {
  margin: 0;
  color: var(--primary);
  font-size: 1.35rem;
  font-weight: 900;
}

.admin-form-card-header p {
  margin: 6px 0 0;
  color: var(--text-secondary);
  font-size: 0.95rem;
}

@media (max-width: 768px) {
  .admin-form-card {
    margin: 24px 0;
    padding: 20px;
  }
}
</style>
