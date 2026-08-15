<script setup>
import { computed, ref, onMounted } from 'vue'

const props = defineProps({
  matches: {
    type: Array,
    required: true,
  },
})

const categories = ['SUB6', 'SUB8', 'SUB10', 'SUB12']

const CATEGORY_COLORS = {
  SUB6: 'var(--primary-light)',
  SUB8: 'var(--pitch)',
  SUB10: 'var(--primary)',
  SUB12: 'var(--primary-dark)',
}

const counts = computed(() => {
  const result = {}

  categories.forEach((category) => {
    result[category] = props.matches.filter(
      (match) => match.localTeam?.category === category,
    ).length
  })

  return result
})

const max = computed(() => Math.max(1, ...Object.values(counts.value)))

const leader = computed(() => {
  const withMatches = categories.filter((category) => counts.value[category] > 0)

  if (withMatches.length === 0) {
    return null
  }

  const topCount = Math.max(...withMatches.map((category) => counts.value[category]))
  const topCategories = withMatches.filter((category) => counts.value[category] === topCount)

  // A tie for first place keeps every category on its own color — only a
  // sole leader gets the gold highlight.
  return topCategories.length === 1 ? topCategories[0] : null
})

const totalMatches = computed(() => props.matches.length)

const leaderPct = computed(() => {
  if (!leader.value || totalMatches.value === 0) {
    return 0
  }

  return Math.round((counts.value[leader.value] / totalMatches.value) * 100)
})

const R = 34
const C = 2 * Math.PI * R

const animated = ref(false)

onMounted(() => {
  if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
    animated.value = true
    return
  }

  requestAnimationFrame(() => {
    requestAnimationFrame(() => {
      animated.value = true
    })
  })
})

function ringOffset(cat) {
  return animated.value ? C - C * (counts.value[cat] / max.value) : C
}

function ringColor(cat) {
  return cat === leader.value ? undefined : CATEGORY_COLORS[cat]
}
</script>

<template>
  <section class="chart-card">
    <div class="chart-header">
      <h3>Tournament Overview</h3>
      <p>Matches distribution by category.</p>
    </div>

    <div class="rings-body">
      <div
        v-for="cat in categories"
        :key="cat"
        class="ring-item"
        :class="{ leader: cat === leader }"
      >
        <span v-if="cat === leader" class="crown">
          <i class="fa-solid fa-crown"></i>
        </span>

        <svg width="86" height="86" viewBox="0 0 86 86">
          <circle class="ring-track" cx="43" cy="43" :r="R" />
          <circle
            class="ring-fill"
            :class="{ 'is-leader': cat === leader }"
            cx="43"
            cy="43"
            :r="R"
            :stroke-dasharray="C"
            :style="{ strokeDashoffset: ringOffset(cat), stroke: ringColor(cat) }"
          />
        </svg>

        <div class="ring-center">
          <div class="n">{{ counts[cat] }}</div>
        </div>

        <div class="ring-label">{{ cat }}</div>
      </div>
    </div>

    <p v-if="leader" class="leader-caption">
      <strong>{{ leader }}</strong> concentra el {{ leaderPct }}% de los partidos programados
      hasta ahora.
    </p>
    <p v-else-if="totalMatches > 0" class="leader-caption empty">
      Varias categorías van igualadas en partidos programados.
    </p>
    <p v-else class="leader-caption empty">Todavía no hay partidos programados.</p>

    <svg width="0" height="0" style="position: absolute">
      <defs>
        <linearGradient id="ringGrad" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stop-color="#d99a2b" />
          <stop offset="100%" stop-color="#b87d1a" />
        </linearGradient>
      </defs>
    </svg>
  </section>
</template>

<style scoped>
.chart-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 16px;
  box-shadow: var(--shadow);
}

.chart-header h3 {
  margin: 0;
  color: var(--primary);
}

.chart-header p {
  margin: 6px 0 0;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.rings-body {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: flex-end;
  gap: 12px;

  padding: 26px 22px 28px;
}

.ring-item {
  position: relative;

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.ring-item.leader {
  transform: scale(1.14);
}

.ring-item svg {
  transform: rotate(-90deg);
}

.ring-track {
  fill: none;
  stroke: var(--background);
  stroke-width: 9;
}

.ring-fill {
  fill: none;
  stroke: var(--accent);
  stroke-width: 9;
  stroke-linecap: round;
  transition: stroke-dashoffset 1s cubic-bezier(0.22, 0.9, 0.3, 1);
}

.ring-fill.is-leader {
  stroke: url(#ringGrad);
}

.ring-center {
  position: absolute;
  top: 50%;
  left: 50%;
  translate: -50% -50%;

  text-align: center;
}

.ring-center .n {
  color: var(--text-primary);
  font-family: var(--font-heading);
  font-size: 1.3rem;
  font-weight: 800;
}

.ring-label {
  color: var(--text-secondary);
  font-size: 0.78rem;
  font-weight: 700;
}

.crown {
  position: absolute;
  top: -14px;
  left: 50%;
  translate: -50% 0;

  width: 22px;
  height: 22px;

  display: flex;
  align-items: center;
  justify-content: center;

  background: #fff;
  border-radius: 50%;
  box-shadow: var(--shadow-sm);

  color: var(--accent-dark);
  font-size: 0.85rem;
}

.leader-caption {
  margin: 0;
  padding: 0 22px 20px;

  color: var(--text-secondary);
  font-size: 0.8rem;
  text-align: center;
}

.leader-caption strong {
  color: var(--accent-dark);
}

@media (prefers-reduced-motion: reduce) {
  .ring-fill {
    transition: none;
  }
}
</style>
