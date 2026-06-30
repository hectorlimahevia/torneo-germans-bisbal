<script setup>
const props = defineProps({
  matchesCount: {
    type: Number,
    required: true,
  },
  teamsCount: {
    type: Number,
    required: true,
  },
  fieldsCount: {
    type: Number,
    required: true,
  },
})
</script>

<template>
  <div class="admin-stats">
    <div class="stat-card">
      <i class="fas fa-clipboard-check"></i>
      <span>{{ props.matchesCount }}</span>
      <p>Matches</p>
    </div>

    <div class="stat-card">
      <i class="fas fa-people-group"></i>
      <span>{{ props.teamsCount }}</span>
      <p>Teams</p>
    </div>

    <div class="stat-card">
      <i class="fas fa-location-dot"></i>
      <span>{{ props.fieldsCount }}</span>
      <p>Fields</p>
    </div>
  </div>
</template>

<style scoped>
.admin-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 2rem;
}

.stat-card {
  background: var(--primary-light);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 18px 12px;
  text-align: center;
  box-shadow: var(--shadow);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease,
    background 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-3px);
  background: var(--primary);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.stat-card span {
  display: block;
  font-size: 2rem;
  font-weight: 800;
  color: #fff;
}

.stat-card p {
  margin: 6px 0 0;
  color: #e7e3e3;
  font-size: 0.9rem;
}

.fa-clipboard-check,
.fa-location-dot,
.fa-people-group {
  color: #fff;
  font-size: 1.7rem;
  margin-bottom: 6px;
}

.admin-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

@media (min-width: 768px) {
  .admin-stats {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (min-width: 1200px) {
  .admin-stats {
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
  }
}
</style>
