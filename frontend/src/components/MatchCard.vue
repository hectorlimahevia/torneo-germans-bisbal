<script setup>
import StatusBadge from '@/components/StatusBadge.vue'
const props = defineProps({
  match: {
    type: Object,
    required: true,
  },
})

function formatTime(time) {
  return time.substring(0, 5)
}

</script>

<template>
<article class="match-card">
  <div class="match-top">
    <div>
      <strong>{{ props.match.field.name }}</strong>
      <p>
        {{ formatTime(props.match.startTime) }} -
        {{ formatTime(props.match.endTime) }}
      </p>
    </div>

    <StatusBadge :status="props.match.status" />
  </div>

  <div class="match-teams">
    <div class="team">
      <img
        :src="props.match.localTeam.club.logoUrl"
        :alt="props.match.localTeam.club.name"
      >
      <span>{{ props.match.localTeam.name }}</span>
    </div>

    <div class="score">
      {{ props.match.localTries }} - {{ props.match.visitorTries }}
    </div>

    <div class="team">
      <img
        :src="props.match.visitorTeam.club.logoUrl"
        :alt="props.match.visitorTeam.club.name"
      >
      <span>{{ props.match.visitorTeam.name }}</span>
    </div>
  </div>

  <div class="match-bottom">
    <span>Round {{ props.match.roundNumber }}</span>
    <span>{{ props.match.matchDate }}</span>
  </div>
</article>
</template>

<style scoped>
.match-card {
  background: var(--card);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 16px;
  border: 1px solid var(--border);
}

.match-top,
.match-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.match-top strong {
  color: var(--primary);
  font-size: 16px;
}

.match-top p {
  margin: 4px 0 0;
  font-size: 14px;
  color: var(--text-secondary);
}

.match-teams {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 12px;
  margin: 20px 0;
}

.team {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  text-align: center;
  font-weight: 700;
}

.team img {
  width: 48px;
  height: 48px;
  object-fit: contain;
}

.score {
  background: var(--primary);
  color: white;
  padding: 8px 14px;
  border-radius: 999px;
  font-size: 22px;
  font-weight: 900;
  white-space: nowrap;
}

.match-bottom {
  border-top: 1px solid var(--border);
  padding-top: 12px;
  color: var(--text-secondary);
  font-size: 13px;
}
</style>
