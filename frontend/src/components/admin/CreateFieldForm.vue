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

</style>
