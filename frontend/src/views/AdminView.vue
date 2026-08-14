<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'

import AdminStats from '@/components/admin/AdminStats.vue'
import AdminOverview from '@/components/admin/AdminOverview.vue'
import AdminTabs from '@/components/admin/AdminTabs.vue'
import ScheduleMatchForm from '@/components/admin/ScheduleMatchForm.vue'
import UpdateMatchForm from '@/components/admin/UpdateMatchForm.vue'
import CreateFieldForm from '@/components/admin/CreateFieldForm.vue'
import CreateClubForm from '@/components/admin/CreateClubForm.vue'
import CreateTeamForm from '@/components/admin/CreateTeamForm.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'
import AdminChart from '@/components/admin/AdminChart.vue'
import AdminUsers from '@/components/admin/AdminUsers.vue'

import { useToast } from '@/composables/useToast'

const teams = ref([])
const fields = ref([])
const clubs = ref([])
const matches = ref([])
const users = ref([])

const scheduleResetKey = ref(0)
const updateResetKey = ref(0)
const fieldResetKey = ref(0)
const clubResetKey = ref(0)
const teamResetKey = ref(0)
const selectedAdminTab = ref('club')

const loading = ref({
  createMatch: false,
  updateMatch: false,
  createField: false,
  createClub: false,
  createTeam: false,
  deleteItem: false,
})

const showConfirmModal = ref(false)

const confirmConfig = ref({
  type: '',
  id: null,
})

const roleAction = ref({
  type: '',
  user: null,
})

const { showToast } = useToast()

async function loadData() {
  try {
    const [teamsResponse, fieldsResponse, clubsResponse, matchesResponse, usersResponse] =
      await Promise.all([
        api.get('/api/teams'),
        api.get('/api/fields'),
        api.get('/api/clubs'),
        api.get('/api/matches'),
        api.get('/api/users'),
      ])

    teams.value = teamsResponse.data
    fields.value = fieldsResponse.data
    clubs.value = clubsResponse.data
    matches.value = matchesResponse.data
    users.value = usersResponse.data
  } catch {
    showToast('Could not load admin data', 'error')
  }
}

function getErrorMessage(err, fallbackMessage) {
  return err.response?.data?.message || fallbackMessage
}

async function createMatch(matchData) {
  loading.value.createMatch = true

  try {
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
    await api.post('/api/fields', fieldData)

    showToast('Field created successfully', 'success')
    fieldResetKey.value++

    await loadData()
  } catch (err) {
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

async function createClub(clubData) {
  loading.value.createClub = true

  try {
    await api.post('/api/clubs', clubData)

    showToast('Club created successfully', 'success')
    clubResetKey.value++

    await loadData()
  } catch (err) {
    showToast(getErrorMessage(err, 'Could not create club'), 'error')
  } finally {
    loading.value.createClub = false
  }
}

function deleteClub(clubId) {
  if (!clubId) {
    showToast('Please select a club', 'error')
    return
  }

  confirmConfig.value = {
    type: 'club',
    id: clubId,
  }

  showConfirmModal.value = true
}

async function createTeam(teamData) {
  loading.value.createTeam = true

  try {
    await api.post('/api/teams', teamData)

    showToast('Team created successfully', 'success')
    teamResetKey.value++

    await loadData()
  } catch (err) {
    showToast(getErrorMessage(err, 'Could not create team'), 'error')
  } finally {
    loading.value.createTeam = false
  }
}

function deleteTeam(teamId) {
  if (!teamId) {
    showToast('Please select a team', 'error')
    return
  }

  confirmConfig.value = {
    type: 'team',
    id: teamId,
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

    if (confirmConfig.value.type === 'club') {
      await api.delete(`/api/clubs/${confirmConfig.value.id}`)

      showToast('Club deleted successfully', 'success')

      clubResetKey.value++
    }

    if (confirmConfig.value.type === 'team') {
      await api.delete(`/api/teams/${confirmConfig.value.id}`)

      showToast('Team deleted successfully', 'success')

      teamResetKey.value++
    }

    closeConfirmModal()

    await loadData()
  } catch (err) {

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

  resetRoleAction()
}

function makeAdmin(user) {
  roleAction.value = {
    type: 'make-admin',
    user,
  }

  confirmConfig.value = {
    type: 'role',
    id: user.id,
  }

  showConfirmModal.value = true
}

function removeAdmin(user) {
  roleAction.value = {
    type: 'remove-admin',
    user,
  }

  confirmConfig.value = {
    type: 'role',
    id: user.id,
  }

  showConfirmModal.value = true
}

async function confirmRoleAction() {
  const user = roleAction.value.user

  if (!user) {
    return
  }

  try {
    if (roleAction.value.type === 'make-admin') {
      await api.post('/api/roles/add-to-user', {
        username: user.username,
        roleName: 'ROLE_ADMIN',
      })

      showToast('Admin role assigned successfully', 'success')
    }

    if (roleAction.value.type === 'remove-admin') {
      await api.post('/api/roles/remove-from-user', {
        username: user.username,
        roleName: 'ROLE_ADMIN',
      })

      showToast('Admin role removed successfully', 'success')
    }

    closeConfirmModal()
    resetRoleAction()

    await loadData()
  } catch (err) {

    showToast(getErrorMessage(err, 'Could not update user role'), 'error')
  }
}

function resetRoleAction() {
  roleAction.value = {
    type: '',
    user: null,
  }
}

async function handleConfirmModal() {
  if (confirmConfig.value.type === 'role') {
    await confirmRoleAction()
    return
  }

  await confirmDelete()
}

onMounted(loadData)
</script>

<template>
  <section class="admin-view">
    <div class="app-container">
      <header class="admin-header">
        <span class="admin-kicker">Dashboard</span>
        <h2>Admin Panel</h2>
        <p>Manage matches, fields, users and tournament data.</p>
      </header>

      <AdminStats
        :matches-count="matches.length"
        :teams-count="teams.length"
        :fields-count="fields.length"
        :clubs-count="clubs.length"
      />

      <AdminChart :matches="matches" />

      <AdminOverview :clubs="clubs" :teams="teams" />

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

      <CreateClubForm
        v-if="selectedAdminTab === 'club'"
        :key="clubResetKey"
        :clubs="clubs"
        :reset-key="clubResetKey"
        :is-loading="loading.createClub"
        @club-created="createClub"
        @club-deleted="deleteClub"
      />

      <CreateTeamForm
        v-if="selectedAdminTab === 'team'"
        :key="teamResetKey"
        :teams="teams"
        :clubs="clubs"
        :reset-key="teamResetKey"
        :is-loading="loading.createTeam"
        @team-created="createTeam"
        @team-deleted="deleteTeam"
      />

      <AdminUsers
        v-if="selectedAdminTab === 'users'"
        :users="users"
        @make-admin="makeAdmin"
        @remove-admin="removeAdmin"
      />

      <ConfirmModal
        v-if="showConfirmModal"
        :title="
          confirmConfig.type === 'role'
            ? roleAction.type === 'make-admin'
              ? 'Make Admin'
              : 'Remove Admin'
            : confirmConfig.type === 'field'
              ? 'Delete Field'
              : confirmConfig.type === 'club'
                ? 'Delete Club'
                : confirmConfig.type === 'team'
                  ? 'Delete Team'
                  : 'Delete Match'
        "
        :message="
          confirmConfig.type === 'role'
            ? roleAction.type === 'make-admin'
              ? `Are you sure you want to make ${roleAction.user?.name} an admin?`
              : `Are you sure you want to remove admin permissions from ${roleAction.user?.name}?`
            : confirmConfig.type === 'field'
              ? 'Are you sure you want to delete this field? This action cannot be undone.'
              : confirmConfig.type === 'club'
                ? 'Are you sure you want to delete this club? This action cannot be undone.'
                : confirmConfig.type === 'team'
                  ? 'Are you sure you want to delete this team? This action cannot be undone.'
                  : 'Are you sure you want to delete this match? This action cannot be undone.'
        "
        :confirm-text="
          confirmConfig.type === 'role'
            ? roleAction.type === 'make-admin'
              ? 'Make Admin'
              : 'Remove Admin'
            : 'Delete'
        "
        cancel-text="Cancel"
        :danger="confirmConfig.type === 'role' ? roleAction.type === 'remove-admin' : true"
        :is-loading="loading.deleteItem"
        @confirm="handleConfirmModal"
        @cancel="closeConfirmModal"
      />
    </div>
  </section>
</template>

<style scoped>
.admin-view {
  min-height: 100vh;
  padding: 32px 0 48px;
}

.admin-header {
margin-top: 0.5rem;
  margin-bottom: 8px;
}

.admin-kicker {
  display: block;
  margin-bottom: 6px;
  color: var(--primary-light);
  font-size: 0.8rem;
  font-weight: 900;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}

.admin-header h2 {
  margin: 0;
  color: var(--primary);
  font-size: 2rem;
  font-weight: 900;
}

.admin-header p {
  margin: 8px 0 0;
  color: var(--text-secondary);
  font-size: 0.95rem;
}

.error-message {
  color: red;
  font-weight: 700;
}
</style>
