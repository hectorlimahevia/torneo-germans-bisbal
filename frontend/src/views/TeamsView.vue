<script setup>
import { ref, onMounted, computed } from 'vue'
import CategoryTabs from '@/components/CategoryTabs.vue'
import { CATEGORIES } from '@/constants/categories.js'
import api from '@/api/api'

const teams = ref([])
const error = ref('')
const selectedCategory = ref('SUB6')
const categories = CATEGORIES

const filteredTeams = computed(() => {
  return teams.value.filter((team) => team.category === selectedCategory.value)
})

async function loadTeams() {
  try {
    const response = await api.get('/api/teams')
    teams.value = response.data
  } catch (err) {
    console.error(err)
    error.value = 'No se pudieron cargar los equipos'
  }
}

onMounted(loadTeams)
</script>

<template>
  <section class="principal-section">
    <h2>Teams</h2>

    <CategoryTabs
      :categories="categories"
      :selected-category="selectedCategory"
      @category-selected="selectedCategory = $event"
    />

    <p v-if="error">{{ error }}</p>

    <div
      v-for="team in filteredTeams"
      :key="team.id"
      class="team-card"
    >
      <img
        :src="team.club.logoUrl"
        :alt="team.club.name"
      >

      <div>
        <h3>{{ team.name }}</h3>
        <p>{{ team.category }}</p>
        <p>{{ team.club.name }} · {{ team.club.city }}</p>
      </div>
    </div>
  </section>
</template>

<style scoped>
.principal-section {
  display: flex;
  flex-direction: column;
  margin-top: 4rem;
  gap: 16px;
}

h2 {
  color: var(--primary);
  margin-bottom: 16px;
}

.team-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 16px;
}

.team-card img {
  width: 56px;
  height: 56px;
  object-fit: contain;
  flex-shrink: 0;
}

.team-card h3 {
  margin: 0 0 4px;
  color: var(--primary);
  font-size: 18px;
}

.team-card p {
  margin: 4px 0;
  color: var(--text-secondary);
}
</style>
