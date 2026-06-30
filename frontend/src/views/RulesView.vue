<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/api/api'

const rules = ref([])
const error = ref('')
const selectedRuleType = ref('scoring')

const scoringRules = computed(() => {
  return rules.value.filter((rule) => rule.points !== undefined)
})

const scheduleRules = computed(() => {
  return rules.value.filter((rule) => rule.category !== undefined)
})

const filteredRules = computed(() => {
  return selectedRuleType.value === 'scoring' ? scoringRules.value : scheduleRules.value
})

async function loadRules() {
  try {
    const response = await api.get('/api/rules')
    rules.value = response.data
  } catch (err) {
    console.error(err)
    error.value = 'Could not load rules'
  }
}

onMounted(loadRules)
</script>

<template>
  <section class="principal-section">
    <h2>Rules</h2>

    <p>
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam interdum massa vitae metus
      feugiat consectetur. Nam hendrerit cursus quam sed lacinia. Curabitur diam ex, condimentum
      eleifend magna eget, scelerisque imperdiet dui. Phasellus egestas arcu id aliquam ornare. Nunc
      a erat et purus tristique euismod. Aenean magna dui, faucibus vitae justo sollicitudin,
      hendrerit imperdiet tellus. Integer ipsum nisl, ultricies ac lobortis id, finibus at augue.

    </p>

    <div class="rule-tabs">
      <button
        type="button"
        :class="{ active: selectedRuleType === 'scoring' }"
        @click="selectedRuleType = 'scoring'"
      ><i class="fa-solid fa-medal"></i>
        Scoring System
      </button>

      <button
        type="button"
        :class="{ active: selectedRuleType === 'schedule' }"
        @click="selectedRuleType = 'schedule'"
      ><i class="fas fa-calendar-days"></i>
        Category Schedule
      </button>
    </div>

    <p v-if="error">
      {{ error }}
    </p>

    <div v-for="rule in filteredRules" :key="rule.id" class="rule-card">
      <div class="rule-header">
        {{ rule.title }}
      </div>

      <p class="rule-description">
        {{ rule.description }}
      </p>

      <span v-if="rule.points !== undefined" class="points-badge"> +{{ rule.points }} pts </span>

      <span v-if="rule.category" class="category-badge">
        {{ rule.category }}
      </span>
    </div>
  </section>
</template>

<style scoped>
.principal-section {
  margin-top: 4rem;
}

h2 {
  color: var(--primary);
  margin-bottom: 16px;
}

.rule-tabs {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  border-bottom: 1px solid var(--border);
  padding-bottom: 8px;
}

.rule-tabs button {
  background: none;
  border: none;
  color: var(--text-secondary);
  font-weight: 800;
  white-space: nowrap;
  padding: 10px 0;
  position: relative;
  cursor: pointer;
}

.rule-tabs button.active {
  color: var(--primary);
}

.rule-tabs button.active::after {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  bottom: -9px;
  height: 4px;
  background: var(--primary);
  border-radius: 999px;
}

.rule-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 16px;
  margin-top: 5%;
  box-shadow: var(--shadow);

  display: flex;
  flex-direction: column;
  gap: 10px;
}

.rule-header {
  font-size: 18px;
  font-weight: 800;
  color: var(--primary);
}

.rule-description {
  margin: 0;
  color: var(--text-secondary);
}

.points-badge,
.category-badge {
  width: fit-content;
  padding: 6px 12px;
  border-radius: 999px;
  background: var(--primary-light);
  color: white;
  font-size: 12px;
  font-weight: 700;
}
</style>
