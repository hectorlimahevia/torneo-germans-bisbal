<script setup>
import { CATEGORIES } from '@/constants/categories.js'
import { ref, computed, watch } from 'vue'

const props = defineProps({
  teams: {
    type: Array,
    required: true,
  },

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

const emit = defineEmits(['match-created'])

const selectedCategory = ref('')

const newMatch = ref({
  localTeamId: '',
  visitorTeamId: '',
  fieldId: '',
  matchDate: '',
  startTime: '',
  endTime: '',
  localTries: 0,
  visitorTries: 0,
  status: 'SCHEDULED',
  roundNumber: 1,
})

const filteredTeams = computed(() => {
  if (!selectedCategory.value) {
    return []
  }

  return props.teams.filter((team) => team.category === selectedCategory.value)
})

const visitorTeams = computed(() => {
  return filteredTeams.value.filter((team) => team.id !== newMatch.value.localTeamId)
})

function submitMatch() {
  emit('match-created', {
    ...newMatch.value,
  })
}

function resetSelectedTeams() {
  newMatch.value.localTeamId = ''
  newMatch.value.visitorTeamId = ''
}

function resetForm() {
  selectedCategory.value = ''

  newMatch.value = {
    localTeamId: '',
    visitorTeamId: '',
    fieldId: '',
    matchDate: '',
    startTime: '',
    endTime: '',
    localTries: 0,
    visitorTries: 0,
    status: 'SCHEDULED',
    roundNumber: 1,
  }
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
    <h3>Create Match</h3>

    <div class="form-group">
      <label for="category"> Category </label>

      <select id="category" v-model="selectedCategory" @change="resetSelectedTeams">
        <option value="">Select category</option>

        <option v-for="category in CATEGORIES" :key="category" :value="category">
          {{ category }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="localTeam"> Local Team </label>

      <select id="localTeam" v-model="newMatch.localTeamId">
        <option value="">Select local team</option>

        <option v-for="team in filteredTeams" :key="team.id" :value="team.id">
          {{ team.name }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="visitorTeam"> Visitor Team </label>

      <select id="visitorTeam" v-model="newMatch.visitorTeamId">
        <option value="">Select visitor team</option>

        <option v-for="team in visitorTeams" :key="team.id" :value="team.id">
          {{ team.name }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="field"> Field </label>

      <select id="field" v-model="newMatch.fieldId">
        <option value="">Select field</option>

        <option v-for="field in props.fields" :key="field.id" :value="field.id">
          {{ field.name }} - {{ field.location }}
        </option>
      </select>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label for="matchDate"> Match Date </label>

        <input id="matchDate" v-model="newMatch.matchDate" type="date" />
      </div>

      <div class="form-group">
        <label for="roundNumber"> Round </label>

        <input id="roundNumber" v-model="newMatch.roundNumber" type="number" min="1" />
      </div>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label for="startTime"> Start Time </label>

        <input id="startTime" v-model="newMatch.startTime" type="time" />
      </div>

      <div class="form-group">
        <label for="endTime"> End Time </label>

        <input id="endTime" v-model="newMatch.endTime" type="time" />
      </div>
    </div>

    <button type="button" class="create-button" :disabled="props.isLoading" @click="submitMatch">
      {{ props.isLoading ? 'Creating...' : 'Create Match' }}
    </button>
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

.form-group select,
.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--border);
  border-radius: 8px;
  background: white;
  font-family: inherit;
  font-size: 14px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
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
