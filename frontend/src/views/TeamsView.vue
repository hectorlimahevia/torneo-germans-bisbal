<script setup>
import { ref, onMounted, computed } from 'vue'
import CategoryTabs from '@/components/CategoryTabs.vue'
import { CATEGORIES } from '@/constants/categories.js'
import { useToast } from '@/composables/useToast'
import api from '@/api/api'

const teams = ref([])
const { showToast } = useToast()
const selectedCategory = ref('SUB6')
const categories = CATEGORIES

const filteredTeams = computed(() => {
  return teams.value.filter((team) => team.category === selectedCategory.value)
})

async function loadTeams() {
  try {
    const response = await api.get('/api/teams')
    teams.value = response.data
  } catch {
    showToast('Could not load teams', 'error')
  }
}

onMounted(loadTeams)
</script>

<template>
  <section class="principal-section">
    <div class="app-container">
      <h2>Teams</h2>

      <CategoryTabs
        :categories="categories"
        :selected-category="selectedCategory"
        @category-selected="selectedCategory = $event"
      />

      <div class="teams-list">
        <div v-for="team in filteredTeams" :key="team.id" class="team-card">
          <img :src="team.club.logoUrl" :alt="team.club.name" />

          <div>
            <h3>{{ team.name }}</h3>
            <p>{{ team.category }}</p>
            <p>{{ team.club.name }} · {{ team.club.city }}</p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.principal-section {
  display: flex;
  flex-direction: column;
  gap: 20px;

  margin-top: 5rem;
}

h2 {
  color: var(--primary);
  margin-bottom: 16px;
}

.teams-list {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
}

.team-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 18px;
  background: var(--card);
  border: 1px solid #3a9dc4;
  border-left: 8px solid var(--primary-light);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.team-card img {
  width: 62px;
  height: 62px;
  margin-right: 8px;
  object-fit: contain;
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

@media (min-width: 900px) {
  .teams-list {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
  }

  .team-card {
    min-height: 125px;
    padding: 22px;
  }

  .team-card img {
    width: 66px;
    height: 66px;
  }

  .team-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 22px rgba(0, 0, 0, 0.12);
  }
}
</style>
