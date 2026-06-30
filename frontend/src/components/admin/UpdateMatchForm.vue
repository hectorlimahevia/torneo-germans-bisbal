<script setup>
import { ref, computed, watch } from 'vue'
import { CATEGORIES } from '@/constants/categories.js'

const props = defineProps({
  matches: {
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

const emit = defineEmits(['match-updated', 'match-deleted'])

const updateCategory = ref('')
const selectedMatchId = ref('')

const matchUpdate = ref({
  localTries: 0,
  visitorTries: 0,
  status: 'FINISHED',
})

function resetForm() {
  updateCategory.value = ''
  selectedMatchId.value = ''

  matchUpdate.value = {
    localTries: 0,
    visitorTries: 0,
    status: 'FINISHED',
  }
}

const filteredMatches = computed(() => {
  if (!updateCategory.value) {
    return []
  }

  return props.matches.filter((match) => match.localTeam?.category === updateCategory.value)
})

function submitUpdateMatch() {
  emit('match-updated', {
    selectedMatchId: selectedMatchId.value,
    matchUpdate: {
      ...matchUpdate.value,
    },
  })
}

function submitDeleteMatch() {
  emit('match-deleted', selectedMatchId.value)
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
    <h3>Update Match Result</h3>

    <div class="form-group">
      <label for="updateCategory"> Category </label>

      <select id="updateCategory" v-model="updateCategory">
        <option value="">Select category</option>

        <option v-for="category in CATEGORIES" :key="category" :value="category">
          {{ category }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="match"> Match </label>

      <select id="match" v-model="selectedMatchId">
        <option value="">Select match</option>

        <option v-for="match in filteredMatches" :key="match.id" :value="match.id">
          {{ match.localTeam?.name || 'Unknown local team' }}
          vs
          {{ match.visitorTeam?.name || 'Unknown visitor team' }}
          · {{ match.matchDate }} · {{ match.startTime?.substring(0, 5) || '--:--' }}
        </option>
      </select>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label for="localTries"> Local Tries </label>

        <input id="localTries" v-model="matchUpdate.localTries" type="number" min="0" />
      </div>

      <div class="form-group">
        <label for="visitorTries"> Visitor Tries </label>

        <input id="visitorTries" v-model="matchUpdate.visitorTries" type="number" min="0" />
      </div>
    </div>

    <div class="form-group">
      <label for="status"> Status </label>

      <select id="status" v-model="matchUpdate.status">
        <option value="SCHEDULED">SCHEDULED</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="FINISHED">FINISHED</option>
        <option value="CANCELLED">CANCELLED</option>
      </select>
    </div>

    <button
      type="button"
      class="create-button"
      :disabled="props.isLoading"
      @click="submitUpdateMatch"
    >
      {{ props.isLoading ? 'Updating...' : 'Update Match' }}
    </button>

    <button type="button" class="delete-button" @click="submitDeleteMatch">Delete Match</button>
  </form>
</template>

<style scoped>

</style>
