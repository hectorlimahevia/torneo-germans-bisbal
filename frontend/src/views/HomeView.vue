<script setup>
import { ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import autovivoLogo from '@/assets/autovivo_logo.jpg'
import bitmatLogo from '@/assets/bitmat_logo.png'
import neopequesLogo from '@/assets/neopeques_logo.png'
import lecoqLogo from '@/assets/lecoq_logo.jpg'
import api from '@/api/api'

const matches = ref([])
const error = ref('')

const sponsors = [
  { name: 'Autovivo', logo: autovivoLogo },
  { name: 'Bitmat', logo: bitmatLogo },
  { name: 'Neopeques', logo: neopequesLogo },
  { name: 'Le Coq Sportif', logo: lecoqLogo },
]

const currentSponsor = ref(0)

function nextSponsor() {
  currentSponsor.value = (currentSponsor.value + 1) % sponsors.length
}

function previousSponsor() {
  currentSponsor.value = (currentSponsor.value - 1 + sponsors.length) % sponsors.length
}

function goToSponsor(index) {
  currentSponsor.value = index
}

function getSponsorClass(index) {
  const total = sponsors.length
  const previousIndex = (currentSponsor.value - 1 + total) % total
  const nextIndex = (currentSponsor.value + 1) % total

  if (index === currentSponsor.value) return 'active'
  if (index === previousIndex) return 'previous'
  if (index === nextIndex) return 'next'

  return 'hidden'
}

async function loadHomeData() {
  try {
    const matchesResponse = await api.get('/api/matches')

    matches.value = matchesResponse.data.slice(0, 3)
  } catch (err) {
    console.error(err)
    error.value = 'Could not load home data'
  }
}
onMounted(loadHomeData)
</script>

<template>
  <section class="home">
    <section class="home-hero">
      <img src="@/assets/logo_ues.png" alt="Torneo Germans Bisbal" class="hero-logo" />

      <div class="hero-title">
        <span class="hero-kicker"> TORNEO </span>

        <h1>
          GERMANS<br />
          BISBAL
        </h1>

        <p class="hero-slogan">PASSIÓ PEL RUGBY</p>
      </div>
    </section>

    <div class="quick-nav">
      <RouterLink to="/standings"> Standings </RouterLink>

      <RouterLink to="/matches"> Matches </RouterLink>

      <RouterLink to="/teams"> Teams </RouterLink>

      <RouterLink to="/rules"> Rules </RouterLink>
    </div>

    <section class="home-section">
      <div class="section-header">
        <h2>Latest Matches</h2>
        <RouterLink to="/matches">View all</RouterLink>
      </div>

      <div class="mini-list">
        <div v-for="match in matches" :key="match.id" class="mini-card">
          <strong> {{ match.localTeam.name }} vs {{ match.visitorTeam.name }} </strong>

          <span> {{ match.field.name }} · {{ match.startTime.substring(0, 5) }} </span>
        </div>
      </div>
    </section>

    <section class="poster-section">
      <img
        src="@/assets/cartel_torneo.jpg"
        alt="Torneo Germans Bisbal poster"
        class="poster-image"
      />
    </section>

    <section class="about-section">
      <h2>About the Tournament</h2>

      <p>
        The Germans Bisbal Tournament is a celebration of grassroots rugby, sportsmanship and
        teamwork. Organised by the U.E. Santboiana rugby school, it brings together young players
        from different clubs to enjoy a day of competition, friendship and rugby values.
      </p>

      <p>
        U.E. Santboiana is one of the most historic rugby clubs in Spain and has played a key role
        in the development of rugby for generations. The tournament reflects this commitment to
        education, respect and passion for the sport.
      </p>
    </section>

    <section class="sponsors-section">
      <h2>Sponsors</h2>

      <div class="coverflow-carousel">
        <button type="button" class="carousel-arrow left" @click="previousSponsor">‹</button>

        <div class="sponsor-stage">
          <article
            v-for="(sponsor, index) in sponsors"
            :key="sponsor.name"
            class="sponsor-card"
            :class="getSponsorClass(index)"
          >
            <img :src="sponsor.logo" :alt="sponsor.name" />

            <span>{{ sponsor.name }}</span>
          </article>
        </div>

        <button type="button" class="carousel-arrow right" @click="nextSponsor">›</button>
      </div>

      <div class="carousel-dots">
        <button
          v-for="(sponsor, index) in sponsors"
          :key="sponsor.name"
          type="button"
          class="dot"
          :class="{ active: currentSponsor === index }"
          @click="goToSponsor(index)"
        />
      </div>
    </section>

    <p v-if="error">
      {{ error }}
    </p>
  </section>
</template>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  gap: 24px;
  min-height: 100vh;
}

.home-hero {
  text-align: center;
  padding-top: 20px;
  padding-bottom: 20px;
}

.hero-logo {
  width: 90px;
  height: 90px;
  object-fit: contain;
  margin-bottom: 12px;
}

.hero-kicker {
  display: block;
  color: var(--primary);
  font-size: 13px;
  font-weight: 800;
  letter-spacing: 8px;
  margin-bottom: 6px;
}

.hero-title h1 {
  margin: 0;
  color: var(--primary);
  font-size: 46px;
  line-height: 0.95;
  font-weight: 900;
}

.hero-slogan {
  margin-top: 12px;
  color: #7d6a52;
  font-size: 14px;
  font-weight: 800;
  letter-spacing: 2px;
}

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.quick-actions a {
  background: var(--card);
  color: var(--primary);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 16px;
  text-align: center;
  text-decoration: none;
  font-weight: 800;
  box-shadow: var(--shadow);
}

.home-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-header h2 {
  margin: 0;
  color: var(--primary);
  font-size: 20px;
}

.section-header a {
  color: var(--primary);
  font-weight: 700;
  text-decoration: none;
  font-size: 14px;
}

.mini-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mini-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 14px;

  display: flex;
  flex-direction: column;
  gap: 6px;
}

.mini-card strong {
  color: var(--primary);
}

.mini-card span {
  color: var(--text-secondary);
  font-size: 14px;
}

.quick-nav {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border);
}

.quick-nav a {
  text-decoration: none;
  color: var(--text-secondary);
  font-weight: 800;
  white-space: nowrap;
}

/* poster-section */

.poster-section {
  display: flex;
  justify-content: center;
  margin-top: 4%;
}

.poster-image {
  width: 100%;
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  border: 1px solid var(--border);
}

/* about-section  */

.about-section {
  padding: 8px;
}

.about-section h2 {
  margin-top: 0;

  color: var(--primary);
}

.about-section p {
  line-height: 1.6;

  color: var(--text-secondary);
}

/* sponsors-section  */
.sponsors-section {
  margin-top: 40px;
}

.sponsors-section h2 {
  color: var(--primary);
}

.coverflow-carousel {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
}

.sponsor-stage {
  position: relative;
  width: 100%;
  height: 170px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.sponsor-card {
  position: absolute;
  width: 48%;
  max-width: 260px;
  height: 135px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 18px;
  box-shadow: var(--shadow);

  transition:
    transform 0.35s ease,
    opacity 0.35s ease,
    z-index 0.35s ease;
}

.sponsor-card img {
  max-width: 140px;
  max-height: 60px;
  object-fit: contain;
}

.sponsor-card span {
  color: var(--primary);

  font-size: 0.9rem;
  font-weight: 800;
}

.sponsor-card.active {
  z-index: 3;
  opacity: 1;
  transform: translateX(0) scale(1);
}

.sponsor-card.previous {
  z-index: 2;
  opacity: 0.55;
  transform: translateX(-28%) scale(0.85);
  background: #f1f2f3;
}

.sponsor-card.next {
  z-index: 2;
  opacity: 0.55;
  transform: translateX(28%) scale(0.85);
  background: #f1f2f3;
}

.sponsor-card.hidden {
  z-index: 1;
  opacity: 0;
  pointer-events: none;
  transform: translateX(0) scale(0.7);
}

.carousel-arrow {
  position: absolute;
  z-index: 5;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  padding: 0;
  border: none;
  border-radius: 50%;
  background: var(--primary);
  color: white;
  font-size: 20px;
  font-weight: 700;
  cursor: pointer;
}

.carousel-arrow.left {
  left: 6px;
}

.carousel-arrow.right {
  right: 6px;
}

.carousel-arrow:hover {
  background: var(--primary-light);
  transform: scale(1.08);
}

.carousel-dots {
  display: flex;
  justify-content: center;
  gap: 8px;

  margin-top: 14px;
}

.dot {
  width: 9px;
  height: 9px;
  border: none;
  border-radius: 999px;
  background: #d1d5db;
  cursor: pointer;
  transition:
    width 0.25s ease,
    background 0.25s ease;
}

.dot.active {
  width: 22px;
  background: var(--primary);
}
</style>
