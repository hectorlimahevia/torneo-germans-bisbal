<script setup>
defineProps({
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
  clubsCount: {
    type: Number,
    required: true,
  },
})
</script>

<template>
  <div class="stat-strip">
    <div class="strip-item" style="--kpi-color: var(--accent)">
      <i class="fa-solid fa-clipboard-check"></i>
      <div>
        <div class="num">{{ matchesCount }}</div>
        <div class="label">Matches</div>
      </div>
    </div>

    <div class="strip-item" style="--kpi-color: var(--pitch)">
      <i class="fa-solid fa-people-group"></i>
      <div>
        <div class="num">{{ teamsCount }}</div>
        <div class="label">Teams</div>
      </div>
    </div>

    <div class="strip-item" style="--kpi-color: var(--primary-light)">
      <i class="fa-solid fa-location-dot"></i>
      <div>
        <div class="num">{{ fieldsCount }}</div>
        <div class="label">Fields</div>
      </div>
    </div>

    <div class="strip-item" style="--kpi-color: var(--primary)">
      <i class="fa-solid fa-shield-halved"></i>
      <div>
        <div class="num">{{ clubsCount }}</div>
        <div class="label">Clubs</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.stat-strip {
  display: flex;
  flex-wrap: wrap;

  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow-sm);

  margin-bottom: 2rem;
  overflow: hidden;
}

.strip-item {
  flex: 1;
  min-width: 140px;

  display: flex;
  align-items: center;
  gap: 12px;

  padding: 16px 20px;

  border-right: 1px solid var(--border);
}

.strip-item:last-child {
  border-right: none;
}

.strip-item i {
  width: 40px;
  height: 40px;
  flex-shrink: 0;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background: color-mix(in srgb, var(--kpi-color, var(--accent)) 16%, white);
  color: var(--kpi-color, var(--accent));

  font-size: 1.1rem;
}

.num {
  color: var(--text-primary);
  font-family: var(--font-heading);
  font-size: 1.4rem;
  font-weight: 800;
  line-height: 1;
}

.label {
  margin-top: 4px;
  color: var(--text-secondary);
  font-size: 0.78rem;
}
</style>
