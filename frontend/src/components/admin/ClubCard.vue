<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  club: {
    type: Object,
    required: true,
  },
  teams: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['color-changed'])

const isOpen = ref(false)
const logoFailed = ref(false)
const colorInput = ref(null)

// Hex twins of the brand tokens (--primary-light, --accent-dark, --pitch,
// --primary): used as a fallback palette for clubs without a saved color,
// and kept as plain hex so <input type="color"> can also use them as a value.
const CLUB_COLORS = ['#2f83ab', '#b87d1a', '#1b6b45', '#0f2f52']

function hashString(value) {
  let hash = 0

  for (let i = 0; i < value.length; i++) {
    hash = (hash << 5) - hash + value.charCodeAt(i)
    hash |= 0
  }

  return Math.abs(hash)
}

const autoColor = computed(() => {
  const key = String(props.club.id ?? props.club.name ?? '')
  return CLUB_COLORS[hashString(key) % CLUB_COLORS.length]
})

const crestColor = computed(() => props.club.color || autoColor.value)

const headerTint = computed(() => `color-mix(in srgb, ${crestColor.value} 8%, white)`)

function openColorPicker() {
  colorInput.value?.click()
}

function onColorChange(event) {
  emit('color-changed', { clubId: props.club.id, color: event.target.value })
}

const initials = computed(() => {
  const words = (props.club.name ?? '').trim().split(/\s+/).filter(Boolean)

  if (words.length === 0) {
    return '?'
  }

  if (words.length === 1) {
    return words[0].slice(0, 2).toUpperCase()
  }

  return (words[0][0] + words[1][0]).toUpperCase()
})

const categoriesCount = computed(() => new Set(props.teams.map((team) => team.category)).size)

function toggle() {
  isOpen.value = !isOpen.value
}
</script>

<template>
  <div class="club-card">
    <div class="club-card-head" :style="{ background: headerTint }">
      <div class="club-card-crest-wrap">
        <div class="club-card-crest" :style="{ background: crestColor }">{{ initials }}</div>

        <button
          type="button"
          class="crest-edit"
          title="Change club color"
          @click="openColorPicker"
        >
          <i class="fa-solid fa-pen"></i>
        </button>

        <input
          ref="colorInput"
          type="color"
          class="color-input-hidden"
          :value="crestColor"
          @change="onColorChange"
        />
      </div>

      <div>
        <h4>
          <img
            v-if="club.logoUrl && !logoFailed"
            :src="club.logoUrl"
            :alt="club.name"
            class="mini-logo"
            @error="logoFailed = true"
          />
          {{ club.name }}
        </h4>
        <p class="city">{{ club.city }}</p>
      </div>
    </div>

    <div class="club-card-stats">
      <div><strong>{{ teams.length }}</strong>equipos</div>
      <div><strong>{{ categoriesCount }}</strong>categorías</div>
    </div>

    <button type="button" class="accordion-trigger" :aria-expanded="isOpen" @click="toggle">
      Ver equipos
      <i class="fa-solid fa-chevron-down"></i>
    </button>

    <div class="accordion-panel" :class="{ 'is-open': isOpen }">
      <div>
        <div v-if="teams.length" class="team-chips">
          <div v-for="team in teams" :key="team.id" class="team-chip-b">
            <span>{{ team.name }}</span>
            <span class="pill-b">{{ team.category }}</span>
          </div>
        </div>

        <p v-else class="empty-hint">Sin equipos todavía.</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.club-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease;
}

.club-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.club-card-head {
  display: flex;
  align-items: center;
  gap: 14px;

  padding: 20px;
}

.club-card-crest-wrap {
  position: relative;
  flex-shrink: 0;
}

.club-card-crest {
  width: 56px;
  height: 56px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;
  box-shadow: var(--shadow-sm);

  color: #fff;
  font-family: var(--font-heading);
  font-size: 1rem;
  font-weight: 800;
}

.crest-edit {
  position: absolute;
  right: -2px;
  bottom: -2px;

  width: 22px;
  height: 22px;

  display: flex;
  align-items: center;
  justify-content: center;

  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 50%;
  box-shadow: var(--shadow-sm);

  color: var(--text-secondary);
  font-size: 0.65rem;

  cursor: pointer;
}

.crest-edit:hover {
  color: var(--primary);
}

.color-input-hidden {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  border: none;
  opacity: 0;
  pointer-events: none;
}

.club-card-head h4 {
  display: flex;
  align-items: center;
  gap: 8px;

  margin: 0;

  color: var(--text-primary);
  font-size: 1.05rem;
}

.mini-logo {
  width: 20px;
  height: 20px;
  flex-shrink: 0;

  object-fit: contain;
  border-radius: 4px;
}

.club-card-head .city {
  margin: 2px 0 0;
  color: var(--text-secondary);
  font-size: 0.8rem;
}

.club-card-stats {
  display: flex;
  gap: 16px;

  padding: 14px 20px;

  border-top: 1px solid var(--border);
  border-bottom: 1px solid var(--border);
}

.club-card-stats div {
  color: var(--text-secondary);
  font-size: 0.78rem;
}

.club-card-stats strong {
  display: block;
  color: var(--text-primary);
  font-family: var(--font-heading);
  font-size: 1.1rem;
}

.accordion-trigger {
  width: 100%;

  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 14px 20px;

  background: none;
  border: none;

  color: var(--primary);
  font-family: var(--font-heading);
  font-size: 0.85rem;
  font-weight: 700;

  cursor: pointer;
}

.accordion-trigger i {
  transition: transform 0.3s ease;
}

.accordion-trigger[aria-expanded='true'] i {
  transform: rotate(180deg);
}

.accordion-panel {
  display: grid;
  grid-template-rows: 0fr;

  padding: 0 20px;

  transition: grid-template-rows 0.35s ease;
}

.accordion-panel.is-open {
  grid-template-rows: 1fr;
  padding-bottom: 16px;
}

.accordion-panel > div {
  overflow: hidden;
  min-height: 0;
}

.team-chips {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.team-chip-b {
  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 9px 12px;

  background: var(--background);
  border-radius: var(--radius-sm);

  font-size: 0.82rem;
}

.pill-b {
  padding: 3px 10px;

  background: color-mix(in srgb, var(--pitch) 15%, white);
  border-radius: 999px;

  color: var(--pitch);
  font-size: 0.7rem;
  font-weight: 800;
}

.empty-hint {
  margin: 0;
  padding: 4px 0;
  color: var(--text-secondary);
  font-size: 0.82rem;
  font-style: italic;
}

@media (prefers-reduced-motion: reduce) {
  .club-card,
  .accordion-trigger i,
  .accordion-panel {
    transition: none;
  }
}
</style>
