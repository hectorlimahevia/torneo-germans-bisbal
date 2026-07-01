<script setup>
const props = defineProps({
  categories: {
    type: Array,
    required: true,
  },
  selectedCategory: {
    type: String,
    required: true,
  },
})


const emit = defineEmits(['category-selected'])
</script>

<template>
<div class="category-tabs">
  <button
    v-for="category in props.categories"
    :key="category"
    :class="{ active: props.selectedCategory === category }"
    @click="emit('category-selected', category)"
  >
    {{ category }}
  </button>
</div>
</template>

<style scoped>
.category-tabs {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  margin-bottom: 18px;
  border-bottom: 1px solid var(--border);
}

.category-tabs button {
  background: none;
  border: none;
  padding: 10px 0;
  color: var(--text-secondary);
  font-weight: 800;
  cursor: pointer;
  position: relative;
}

.category-tabs button.active {
  color: var(--primary);
}

.category-tabs button.active::after {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  height: 4px;
  background: var(--primary);
  border-radius: 999px;
}
</style>
