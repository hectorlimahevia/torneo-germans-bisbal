<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/api/api'
import CategoryTabs from '@/components/CategoryTabs.vue'
import MatchCard from '@/components/MatchCard.vue'
import { CATEGORIES } from '@/constants/categories.js'
import { useToast } from '@/composables/useToast'

const matches = ref([])
const { showToast } = useToast()
const categories = CATEGORIES

const selectedCategory = ref('SUB6')

const filteredMatches = computed(() => {
  return matches.value
    .filter((match) => match.localTeam?.category === selectedCategory.value)
    .sort((a, b) => {
      const dateA = new Date(`${a.matchDate}T${a.startTime}`)
      const dateB = new Date(`${b.matchDate}T${b.startTime}`)

      return dateA - dateB
    })
})

async function loadMatches() {
  try {
    const response = await api.get('/api/matches')
    matches.value = response.data
  } catch {
    showToast('Could not load matches.', 'error')
  }
}
onMounted(loadMatches)
</script>

<template>
  <section class="principal-section">
    <div class="app-container">
      <h2>Matches</h2>

      <CategoryTabs
        :categories="categories"
        :selected-category="selectedCategory"
        @category-selected="selectedCategory = $event"
      />

      <MatchCard v-for="match in filteredMatches" :key="match.id" :match="match" />
    </div>
  </section>
</template>

<style scoped>
.principal-section {
  margin-top: 5rem;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.match-card {
  margin: 4%;
}
h2 {
  color: var(--primary);
  margin-bottom: 16px;
}
</style>
