<script setup>
import { computed } from 'vue'
import ChartDataLabels from 'chartjs-plugin-datalabels'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

const props = defineProps({
  matches: {
    type: Array,
    required: true,
  },
})

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ChartDataLabels)

const categories = ['SUB6', 'SUB8', 'SUB10', 'SUB12']

const chartData = computed(() => {
  const counts = categories.map((category) => {
    return props.matches.filter((match) => match.localTeam?.category === category).length
  })

  return {
    labels: categories,
    datasets: [
      {
        label: 'Matches',
        data: counts,
        borderRadius: 999,
        barThickness: 22,
        backgroundColor: 'rgba(58, 157, 196, 0.4)',
      },
    ],
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,

  indexAxis: 'y',

  plugins: {
    legend: {
      display: false,
    },

    title: {
      display: false,
    },

    tooltip: {
      callbacks: {
        label(context) {
          return `${context.raw} matches`
        },
      },
    },
  },

  scales: {
    x: {
      beginAtZero: true,
      ticks: {
        precision: 0,
      },
    },
  },

  datalabels: {
    anchor: 'end',
    align: 'right',
    color: '#0f2742',
    font: {
      weight: 'bold',
      size: 14,
    },
    formatter: (value) => value,
  },
}
</script>

<template>
  <section class="chart-card">
    <div class="chart-header">
      <h3>Tournament Overview</h3>
      <p>Matches distribution by category.</p>
    </div>

    <div class="chart-wrapper">
      <Bar :data="chartData" :options="chartOptions" />
    </div>
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

.chart-wrapper {
  height: 220px;
  margin-top: 16px;
}
</style>
