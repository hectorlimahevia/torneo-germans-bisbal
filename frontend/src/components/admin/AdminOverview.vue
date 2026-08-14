<script setup>
import ClubCard from '@/components/admin/ClubCard.vue'

const props = defineProps({
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

    <div v-if="clubs.length" class="club-grid">
      <ClubCard
        v-for="club in clubs"
        :key="club.id"
        :club="club"
        :teams="teams.filter((team) => team.club?.id === club.id)"
      />
    </div>

    <p v-else class="empty-hint">No clubs created yet.</p>
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

.club-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 18px;

  margin-top: 16px;
}

.empty-hint {
  margin: 16px 0 0;
  color: var(--text-secondary);
  font-size: 0.85rem;
  font-style: italic;
}
</style>
