<script setup>
defineProps({
  clubs: {
    type: Array,
    required: true,
  },

  teams: {
    type: Array,
    required: true,
  },
})
</script>

<template>
  <section class="overview-card">
    <div class="overview-header">
      <h3>Clubs & Teams</h3>
      <p>Everything created so far in the tournament.</p>
    </div>

    <div class="overview-grid">
      <div class="overview-col">
        <h4>
          <i class="fa-solid fa-shield-halved"></i>
          Clubs
          <span class="count-badge">{{ clubs.length }}</span>
        </h4>

        <div v-if="clubs.length" class="clubs-row">
          <div v-for="club in clubs" :key="club.id" class="club-chip">
            <img :src="club.logoUrl" :alt="club.name" />
            <span>{{ club.name }}</span>
          </div>
        </div>

        <p v-else class="empty-hint">No clubs created yet.</p>
      </div>

      <div class="overview-col">
        <h4>
          <i class="fa-solid fa-people-group"></i>
          Teams
          <span class="count-badge">{{ teams.length }}</span>
        </h4>

        <div v-if="teams.length" class="teams-list">
          <div v-for="team in teams" :key="team.id" class="team-chip">
            <img :src="team.club?.logoUrl" :alt="team.club?.name" />

            <div class="team-chip-info">
              <strong>{{ team.name }}</strong>
              <span class="category-pill">{{ team.category }}</span>
            </div>
          </div>
        </div>

        <p v-else class="empty-hint">No teams created yet.</p>
      </div>
    </div>
  </section>
</template>

<style scoped>
.overview-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 16px;
  box-shadow: var(--shadow);
  margin-top: 20px;
}

.overview-header h3 {
  margin: 0;
  color: var(--primary);
}

.overview-header p {
  margin: 6px 0 0;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.overview-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
  margin-top: 16px;
}

.overview-col h4 {
  display: flex;
  align-items: center;
  gap: 8px;

  margin: 0 0 12px;

  color: var(--text-primary);
  font-size: 1rem;
  font-weight: 800;
}

.overview-col h4 i {
  color: var(--accent);
}

.count-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 22px;
  height: 22px;
  padding: 0 6px;

  background: var(--primary-dark);
  border-radius: 999px;

  color: #fff;
  font-size: 0.75rem;
  font-weight: 800;
}

.empty-hint {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.85rem;
  font-style: italic;
}

/* ---------- Clubs ---------- */

.clubs-row {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
}

.club-chip {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  width: 84px;
}

.club-chip img {
  width: 56px;
  height: 56px;

  object-fit: contain;

  background: var(--background);
  border: 1px solid var(--border);
  border-radius: 50%;

  padding: 8px;
}

.club-chip span {
  color: var(--text-primary);
  font-size: 0.75rem;
  font-weight: 700;
  text-align: center;
  line-height: 1.2;
}

/* ---------- Teams ---------- */

.teams-list {
  display: flex;
  flex-direction: column;
  gap: 10px;

  max-height: 260px;
  overflow-y: auto;
  padding-right: 4px;
}

.team-chip {
  display: flex;
  align-items: center;
  gap: 12px;

  padding: 8px 10px;

  background: var(--background);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
}

.team-chip img {
  width: 32px;
  height: 32px;

  object-fit: contain;

  border-radius: 50%;
  background: var(--card);
}

.team-chip-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.team-chip-info strong {
  color: var(--text-primary);
  font-size: 0.9rem;
}

.category-pill {
  padding: 2px 8px;

  background: var(--accent);
  border-radius: 999px;

  color: var(--primary-dark);
  font-size: 0.7rem;
  font-weight: 800;
}

@media (min-width: 768px) {
  .overview-grid {
    grid-template-columns: 1fr 1fr;
    gap: 24px;
  }
}
</style>
