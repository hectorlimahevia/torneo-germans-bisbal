<script setup>
import { CATEGORIES } from '@/constants/categories.js'
import { ref, watch } from 'vue'

const emit = defineEmits(['team-created', 'team-deleted'])

const props = defineProps({
  teams: {
    type: Array,
    required: true,
  },

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

const newTeam = ref({
  name: '',
  category: '',
  clubId: '',
})

const selectedTeamId = ref('')

function resetForm() {
  newTeam.value = {
    name: '',
    category: '',
    clubId: '',
  }

  selectedTeamId.value = ''
}

function submitTeam() {
  emit('team-created', {
    name: newTeam.value.name,
    category: newTeam.value.category,
    club: { id: newTeam.value.clubId },
  })
}

function submitDeleteTeam() {
  emit('team-deleted', selectedTeamId.value)
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
    <h3>Create Team</h3>

    <div class="form-group">
      <label for="teamClub"> Club </label>

      <select id="teamClub" v-model="newTeam.clubId">
        <option value="">Select club</option>

        <option v-for="club in props.clubs" :key="club.id" :value="club.id">
          {{ club.name }} - {{ club.city }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="teamCategory"> Category </label>

      <select id="teamCategory" v-model="newTeam.category">
        <option value="">Select category</option>

        <option v-for="category in CATEGORIES" :key="category" :value="category">
          {{ category }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="teamName"> Team Name </label>

      <input id="teamName" v-model="newTeam.name" type="text" placeholder="UES SUB10" />
    </div>

    <button type="button" class="create-button" :disabled="props.isLoading" @click="submitTeam">
      {{ props.isLoading ? 'Creating...' : 'Create Team' }}
    </button>

    <div class="form-divider"></div>

    <h3>Delete Team</h3>

    <div class="form-group">
      <label for="deleteTeam"> Team </label>

      <select id="deleteTeam" v-model="selectedTeamId">
        <option value="">Select team</option>

        <option v-for="team in props.teams" :key="team.id" :value="team.id">
          {{ team.name }}
        </option>
      </select>
    </div>

    <button type="button" class="delete-button" @click="submitDeleteTeam">Delete Team</button>
  </form>
</template>

<style scoped>

</style>
