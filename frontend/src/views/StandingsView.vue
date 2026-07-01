<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import CategoryTabs from '@/components/CategoryTabs.vue'
import { CATEGORIES } from '@/constants/categories.js'
import StandingCard from '@/components/StandingCard.vue'
import { useToast } from '@/composables/useToast'

const categories = CATEGORIES
const selectedCategory = ref('SUB6')
const standings = ref([])
const { showToast } = useToast()

async function loadStandings() {
  try {
    const response = await api.get(`/api/standings/${selectedCategory.value}`)
    standings.value = response.data
  } catch {
    showToast('Could not load standings', 'error')
  }
}

function handleCategorySelected(category) {
  selectedCategory.value = category
  loadStandings()
}

onMounted(loadStandings)
</script>

<template>
  <section class="principal-section">
    <div class="app-container">
      <h2>Standings</h2>
      <CategoryTabs
        :categories="categories"
        :selected-category="selectedCategory"
        @category-selected="handleCategorySelected"
      />

      <div v-if="standings.length" class="standings-list">
        <StandingCard v-for="standing in standings" :key="standing.teamId" :standing="standing" />
      </div>
    </div>
  </section>
</template>

<style scoped>
.principal-section {
  margin-top: 6rem;
}
h2 {
  color: var(--primary);
  margin-bottom: 16px;
}

.standings-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
