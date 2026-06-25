<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import CategoryTabs from '@/components/CategoryTabs.vue'
import { CATEGORIES } from '@/constants/categories.js'
import StandingCard from '@/components/StandingCard.vue'

const categories = CATEGORIES
const selectedCategory = ref('SUB6')
const standings = ref([])
const error = ref('')

async function loadStandings() {
  try {
    const response = await api.get(`/api/standings/${selectedCategory.value}`)
    standings.value = response.data
  } catch (err) {
    console.error(err)
    error.value = 'Could not load standings'
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

    <h2>Standings</h2>
    <CategoryTabs
      :categories="categories"
      :selected-category="selectedCategory"
      @category-selected="handleCategorySelected"
    />

    <p v-if="error">
      {{ error }}
    </p>

    <div
  v-if="standings.length"
  class="standings-list"
>
  <StandingCard
    v-for="standing in standings"
    :key="standing.teamId"
    :standing="standing"
  />

</div>
  </section>
</template>

<style scoped>

.principal-section{
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
