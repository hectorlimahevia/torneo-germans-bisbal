<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import AdminStats from '@/components/admin/AdminStats.vue'
import AdminTabs from '@/components/admin/AdminTabs.vue'
import ScheduleMatchForm from '@/components/admin/ScheduleMatchForm.vue'
import CreateFieldForm from '@/components/admin/CreateFieldForm.vue'
import UpdateMatchForm from '@/components/admin/UpdateMatchForm.vue'

const teams = ref([])
const fields = ref([])
const matches = ref([])
const selectedAdminTab = ref('create')

const error = ref('')
const success = ref('')

async function loadData() {
  try {
    const [teamsResponse, fieldsResponse, matchesResponse] = await Promise.all([
      api.get('/api/teams'),
      api.get('/api/fields'),
      api.get('/api/matches'),
    ])

    teams.value = teamsResponse.data
    fields.value = fieldsResponse.data
    matches.value = matchesResponse.data
  } catch (err) {
    console.error(err)
    error.value = 'Could not load admin data'
  }
}

function getErrorMessage(err, fallbackMessage) {
  return err.response?.data?.message || fallbackMessage
}

async function createMatch(matchData) {
  try {
    error.value = ''
    success.value = ''

    await api.post('/api/matches', {
      ...matchData,
      localTries: 0,
      visitorTries: 0,
      status: 'SCHEDULED',
    })

    success.value = 'Match created successfully'

    await loadData()
  } catch (err) {
    console.error(err)

    error.value = getErrorMessage(err, 'Could not create match')
  }
}

async function updateMatch(payload) {
  const selectedMatchId = payload.selectedMatchId
  const matchUpdate = payload.matchUpdate

  try {
    error.value = ''
    success.value = ''

    if (!selectedMatchId) {
      error.value = 'Please select a match'
      return
    }

    const selectedMatch = matches.value.find((match) => match.id === selectedMatchId)

    if (!selectedMatch) {
      error.value = 'Selected match not found'
      return
    }

    await api.put(`/api/matches/${selectedMatchId}`, {
      localTeamId: selectedMatch.localTeam?.id,
      visitorTeamId: selectedMatch.visitorTeam?.id,
      fieldId: selectedMatch.field?.id,
      matchDate: selectedMatch.matchDate,
      startTime: selectedMatch.startTime,
      endTime: selectedMatch.endTime,
      roundNumber: selectedMatch.roundNumber,
      localTries: matchUpdate.localTries,
      visitorTries: matchUpdate.visitorTries,
      status: matchUpdate.status,
    })

    success.value = 'Match updated successfully'

    await loadData()
  } catch (err) {
    console.error(err)

    error.value = getErrorMessage(err, 'Could not update match')
  }
}

async function deleteMatch(selectedMatchId) {
  try {
    error.value = ''
    success.value = ''

    if (!selectedMatchId) {
      error.value = 'Please select a match'
      return
    }

    const confirmed = window.confirm('Are you sure you want to delete this match?')

    if (!confirmed) {
      return
    }

    await api.delete(`/api/matches/${selectedMatchId}`)

    success.value = 'Match deleted successfully'

    await loadData()
  } catch (err) {
    console.error(err)

    error.value = getErrorMessage(err, 'Could not delete match')
  }
}

async function createField(fieldData) {
  try {
    error.value = ''
    success.value = ''

    await api.post('/api/fields', fieldData)

    success.value = 'Field created successfully'

    await loadData()
  } catch (err) {
    console.error(err)

    error.value = getErrorMessage(err, 'Could not create field')
  }
}

async function deleteField(fieldId) {
  try {
    error.value = ''
    success.value = ''

    if (!fieldId) {
      error.value = 'Please select a field'
      return
    }

    const confirmed = window.confirm('Are you sure you want to delete this field?')

    if (!confirmed) {
      return
    }

    await api.delete(`/api/fields/${fieldId}`)

    success.value = 'Field deleted successfully'

    await loadData()
  } catch (err) {
    console.error(err)

    error.value = getErrorMessage(err, 'Could not delete field')
  }
}

onMounted(loadData)
</script>

<template>
  <section>
    <h2>Admin Panel</h2>

    <AdminStats
      :matches-count="matches.length"
      :teams-count="teams.length"
      :fields-count="fields.length"
    />

    <AdminTabs :selected-tab="selectedAdminTab" @tab-selected="selectedAdminTab = $event" />

    <ScheduleMatchForm
      v-if="selectedAdminTab === 'create'"
      :teams="teams"
      :fields="fields"
      @match-created="createMatch"
    />

    <UpdateMatchForm
      v-if="selectedAdminTab === 'update'"
      :matches="matches"
      @match-updated="updateMatch"
      @match-deleted="deleteMatch"
    />

    <CreateFieldForm
      v-if="selectedAdminTab === 'field'"
      :fields="fields"
      @field-created="createField"
      @field-deleted="deleteField"
    />

    <p v-if="success" class="success-message">
      {{ success }}
    </p>

    <p v-if="error" class="error-message">
      {{ error }}
    </p>
  </section>
</template>

<style scoped>
section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

h2,
h3 {
  color: var(--primary);
}

.success-message {
  color: green;
  font-weight: 700;
}

.error-message {
  color: red;
  font-weight: 700;
}

</style>
