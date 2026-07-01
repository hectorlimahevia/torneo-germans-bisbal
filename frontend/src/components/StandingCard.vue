<script setup>
const props = defineProps({
  standing: {
    type: Object,
    required: true,
  },
})

function getRankBadge(position) {
  switch (position) {
    case 1:
      return '🥇'
    case 2:
      return '🥈'
    case 3:
      return '🥉'
    default:
      return `#${position}`
  }
}

function formatDifference(value) {
  return value > 0 ? `+${value}` : value
}

</script>

<template>
<article class="standing-card">
  <div class="standing-header">
    <div class="team-info">
      <span class="rank">
          {{ getRankBadge(props.standing.rankPosition) }}
      </span>

      <h3>{{ props.standing.teamName }}</h3>
    </div>

    <div class="points">
      <strong>{{ props.standing.totalPoints }}</strong>
      <span>PTS</span>
    </div>
  </div>

  <div class="standing-stats">
    <span>P {{ props.standing.played }}</span>
    <span>W {{ props.standing.won }}</span>
    <span>D {{ props.standing.drawn }}</span>
    <span>L {{ props.standing.lost }}</span>
  </div>

  <div class="standing-tries">
    <span>TF {{ props.standing.triesFor }}</span>
    <span>TA {{ props.standing.triesAgainst }}</span>
    <span>DIFF {{ formatDifference(props.standing.triesDifference) }}</span>
  </div>
</article>
</template>

<style scoped>
.standing-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.standing-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.team-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.rank {
  color: white;
  background: #264157;
  border-radius: 999px;
  padding: 6px 10px;
  border: 2px solid #3f79d1;
  font-size: 14px;
  font-weight: 900;
}

.team-info h3 {
  margin: 0;
  color: var(--primary);
  font-size: 18px;
}

.points {
  text-align: center;
  color: var(--primary);
}

.points strong {
  display: block;
  font-size: 28px;
  line-height: 1;
}

.points span {
  font-size: 11px;
  font-weight: 800;
}

.standing-stats,
.standing-tries {
  display: flex;
  gap: 8px;
}

.standing-stats span,
.standing-tries span {
  flex: 1;
  text-align: center;
  padding: 8px;
  background: #f5f7fa;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 700;
  color: var(--text-secondary);
}
</style>
