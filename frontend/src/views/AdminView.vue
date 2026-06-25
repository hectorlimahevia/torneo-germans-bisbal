<script setup>
import { ref, onMounted } from 'vue'

import api from '@/api/api'

import AdminStats from '@/components/admin/AdminStats.vue'
import AdminTabs from '@/components/admin/AdminTabs.vue'
import ScheduleMatchForm from '@/components/admin/ScheduleMatchForm.vue'
import UpdateMatchForm from '@/components/admin/UpdateMatchForm.vue'
import CreateFieldForm from '@/components/admin/CreateFieldForm.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'
import AdminChart from '@/components/admin/AdminChart.vue'

import { useToast } from '@/composables/useToast'

const teams = ref([])
const fields = ref([])
const matches = ref([])

const scheduleResetKey = ref(0)
const updateResetKey = ref(0)
const fieldResetKey = ref(0)

const selectedAdminTab = ref('create')
const error = ref('')

const loading = ref({
  createMatch: false,
  updateMatch: false,
  createField: false,
  deleteItem: false,
})

const showConfirmModal = ref(false)

const confirmConfig = ref({
  type: '',
  id: null,
})

const { showToast } = useToast()

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
  loading.value.createMatch = true

  try {
    error.value = ''

    await api.post('/api/matches', {
      ...matchData,
      localTries: 0,
      visitorTries: 0,
      status: 'SCHEDULED',
    })

    showToast('Match created successfully', 'success')

    scheduleResetKey.value++

    await loadData()
  } catch (err) {
    console.error(err)

    showToast(getErrorMessage(err, 'Could not create match'), 'error')
  } finally {
    loading.value.createMatch = false
  }
}

async function updateMatch(payload) {
  loading.value.updateMatch = true

  const selectedMatchId = payload.selectedMatchId
  const matchUpdate = payload.matchUpdate

  try {
    error.value = ''

    if (!selectedMatchId) {
      showToast('Please select a match', 'error')
      return
    }

    const selectedMatch = matches.value.find((match) => match.id === selectedMatchId)

    if (!selectedMatch) {
      showToast('Selected match not found', 'error')
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

    showToast('Match updated successfully', 'success')

    updateResetKey.value++

    await loadData()
  } catch (err) {
    console.error(err)

    showToast(getErrorMessage(err, 'Could not update match'), 'error')
  } finally {
    loading.value.updateMatch = false
  }
}

function deleteMatch(matchId) {
  if (!matchId) {
    showToast('Please select a match', 'error')
    return
  }

  confirmConfig.value = {
    type: 'match',
    id: matchId,
  }

  showConfirmModal.value = true
}

async function createField(fieldData) {
  loading.value.createField = true

  try {
    error.value = ''

    await api.post('/api/fields', fieldData)

    showToast('Field created successfully', 'success')

    fieldResetKey.value++

    await loadData()
  } catch (err) {
    console.error(err)

    showToast(getErrorMessage(err, 'Could not create field'), 'error')
  } finally {
    loading.value.createField = false
  }
}

function deleteField(fieldId) {
  if (!fieldId) {
    showToast('Please select a field', 'error')
    return
  }

  confirmConfig.value = {
    type: 'field',
    id: fieldId,
  }

  showConfirmModal.value = true
}

async function confirmDelete() {
  loading.value.deleteItem = true

  try {
    if (confirmConfig.value.type === 'match') {
      await api.delete(`/api/matches/${confirmConfig.value.id}`)

      showToast('Match deleted successfully', 'success')

      updateResetKey.value++
    }

    if (confirmConfig.value.type === 'field') {
      await api.delete(`/api/fields/${confirmConfig.value.id}`)

      showToast('Field deleted successfully', 'success')

      fieldResetKey.value++
    }

    closeConfirmModal()

    await loadData()
  } catch (err) {
    console.error(err)

    showToast(getErrorMessage(err, 'Could not delete item'), 'error')
  } finally {
    loading.value.deleteItem = false
  }
}

function closeConfirmModal() {
  showConfirmModal.value = false

  confirmConfig.value = {
    type: '',
    id: null,
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

    <AdminChart :matches="matches" />

    <AdminTabs :selected-tab="selectedAdminTab" @tab-selected="selectedAdminTab = $event" />

    <ScheduleMatchForm
      v-if="selectedAdminTab === 'create'"
      :key="scheduleResetKey"
      :teams="teams"
      :fields="fields"
      :reset-key="scheduleResetKey"
      :is-loading="loading.createMatch"
      @match-created="createMatch"
    />

    <UpdateMatchForm
      v-if="selectedAdminTab === 'update'"
      :key="updateResetKey"
      :matches="matches"
      :reset-key="updateResetKey"
      :is-loading="loading.updateMatch"
      @match-updated="updateMatch"
      @match-deleted="deleteMatch"
    />

    <CreateFieldForm
      v-if="selectedAdminTab === 'field'"
      :key="fieldResetKey"
      :fields="fields"
      :reset-key="fieldResetKey"
      :is-loading="loading.createField"
      @field-created="createField"
      @field-deleted="deleteField"
    />

    <p v-if="error" class="error-message">
      {{ error }}
    </p>

    <ConfirmModal
      v-if="showConfirmModal"
      :title="confirmConfig.type === 'field' ? 'Delete Field' : 'Delete Match'"
      :message="
        confirmConfig.type === 'field'
          ? 'Are you sure you want to delete this field? This action cannot be undone.'
          : 'Are you sure you want to delete this match? This action cannot be undone.'
      "
      confirm-text="Delete"
      cancel-text="Cancel"
      :danger="true"
      :is-loading="loading.deleteItem"
      @confirm="confirmDelete"
      @cancel="closeConfirmModal"
    />
  </section>
</template>

<style scoped>
section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

h2 {
  color: var(--primary);
}

.error-message {
  color: red;
  font-weight: 700;
}
</style>
