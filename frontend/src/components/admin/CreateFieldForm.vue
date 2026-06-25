<script setup>
import { ref, watch } from 'vue'
const emit = defineEmits(['field-created', 'field-deleted'])

const props = defineProps({
  fields: {
    type: Array,
    required: true,
  },

  resetKey: {
    type: Number,
    required: true,
  },

  isLoading: {
    type: Boolean,
    default: false,
  },
})

const newField = ref({
  name: '',
  location: '',
})

const selectedFieldId = ref('')

function resetForm() {
  newField.value = {
    name: '',
    location: '',
  }

  selectedFieldId.value = ''
}

function submitField() {
  emit('field-created', {
    ...newField.value,
  })
}

function submitDeleteField() {
  emit('field-deleted', selectedFieldId.value)
}

watch(
  () => props.resetKey,
  () => {
    resetForm()
  },
)
</script>

<template>
  <form class="admin-form">
    <h3>Create Field</h3>

    <div class="form-group">
      <label for="fieldName"> Field Name </label>

      <input id="fieldName" v-model="newField.name" type="text" placeholder="Field 3" />
    </div>

    <div class="form-group">
      <label for="fieldLocation"> Location </label>

      <input id="fieldLocation" v-model="newField.location" type="text" placeholder="Sant Boi" />
    </div>

    <button type="button" class="create-button" :disabled="props.isLoading" @click="submitField">
      {{ props.isLoading ? 'Creating...' : 'Create Field' }}
    </button>

    <div class="form-divider"></div>

    <h3>Delete Field</h3>

    <div class="form-group">
      <label for="deleteField"> Field </label>

      <select id="deleteField" v-model="selectedFieldId">
        <option value="">Select field</option>

        <option v-for="field in props.fields" :key="field.id" :value="field.id">
          {{ field.name }} - {{ field.location }}
        </option>
      </select>
    </div>

    <button type="button" class="delete-button" @click="submitDeleteField">Delete Field</button>
  </form>
</template>

<style scoped>
.admin-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: var(--card);
 padding: 16px;
}


.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-weight: 700;
  color: var(--primary);
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--border);
  border-radius: 8px;
  background: white;
  font-family: inherit;
  font-size: 14px;
}

.create-button {
  align-self: center;
  width: 50%;
  max-width: 260px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: 999px;
  padding: 12px 18px;
  font-weight: 800;
  cursor: pointer;
  transition:
    background 0.2s ease,
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.create-button:hover {
  background: var(--primary-light);
  transform: translateY(-2px);
  box-shadow: var(--shadow);
}

.form-divider {
  height: 1px;
  background: var(--border);
  margin: 12px 0;
}

.delete-button {
  align-self: center;
  width: 50%;
  max-width: 260px;
  background: #dc2626;
  color: white;
  border: none;
  border-radius: 999px;
  padding: 12px 18px;
  font-weight: 800;
  cursor: pointer;
  transition:
    background 0.2s ease,
    transform 0.2s ease;
}

.delete-button:hover {
  background: #b91c1c;
  transform: translateY(-2px);
}

.create-button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.create-button:disabled:hover {
  background: var(--primary);
  transform: none;
  box-shadow: none;
}
</style>
