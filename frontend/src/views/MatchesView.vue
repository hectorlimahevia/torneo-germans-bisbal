<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/api/api'
import CategoryTabs from '@/components/CategoryTabs.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import MatchCard from '@/components/MatchCard.vue'
import { CATEGORIES } from '@/constants/categories.js'

const matches = ref([])
const error = ref('')
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
  } catch (err) {
    console.error(err)
    error.value = 'Could not load matches'
  }
}
onMounted(loadMatches)
</script>

<template>
  <section class="principal-section">
    <h2>Matches</h2>

    <p v-if="error">
      {{ error }}
    </p>

    <CategoryTabs
      :categories="categories"
      :selected-category="selectedCategory"
      @category-selected="selectedCategory = $event"
    />

    <MatchCard v-for="match in filteredMatches" :key="match.id" :match="match" />
  </section>
</template>

<style scoped>
.principal-section {
  margin-top: 6rem;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

h2 {
  color: var(--primary);
  margin-bottom: 16px;
}

</style>
