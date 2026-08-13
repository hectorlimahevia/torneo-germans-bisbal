<script setup>
import { ref, watch } from 'vue'
const emit = defineEmits(['club-created', 'club-deleted'])

const props = defineProps({
  clubs: {
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

const newClub = ref({
  name: '',
  city: '',
  country: '',
  logoUrl: '',
})

const selectedClubId = ref('')

function resetForm() {
  newClub.value = {
    name: '',
    city: '',
    country: '',
    logoUrl: '',
  }

  selectedClubId.value = ''
}

function submitClub() {
  emit('club-created', {
    ...newClub.value,
  })
}

function submitDeleteClub() {
  emit('club-deleted', selectedClubId.value)
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
    <h3>Create Club</h3>

    <div class="form-group">
      <label for="clubName"> Club Name </label>

      <input id="clubName" v-model="newClub.name" type="text" placeholder="UE Santboiana" />
    </div>

    <div class="form-group">
      <label for="clubCity"> City </label>

      <input id="clubCity" v-model="newClub.city" type="text" placeholder="Sant Boi" />
    </div>

    <div class="form-group">
      <label for="clubCountry"> Country </label>

      <input id="clubCountry" v-model="newClub.country" type="text" placeholder="Spain" />
    </div>

    <div class="form-group">
      <label for="clubLogoUrl"> Logo URL </label>

      <input id="clubLogoUrl" v-model="newClub.logoUrl" type="text" placeholder="https://..." />
    </div>

    <button type="button" class="create-button" :disabled="props.isLoading" @click="submitClub">
      {{ props.isLoading ? 'Creating...' : 'Create Club' }}
    </button>

    <div class="form-divider"></div>

    <h3>Delete Club</h3>

    <div class="form-group">
      <label for="deleteClub"> Club </label>

      <select id="deleteClub" v-model="selectedClubId">
        <option value="">Select club</option>

        <option v-for="club in props.clubs" :key="club.id" :value="club.id">
          {{ club.name }} - {{ club.city }}
        </option>
      </select>
    </div>

    <button type="button" class="delete-button" @click="submitDeleteClub">Delete Club</button>
  </form>
</template>

<style scoped>

</style>
