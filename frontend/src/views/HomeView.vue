<script setup>
import { ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import autovivoLogo from '@/assets/autovivo_logo.jpg'
import bitmatLogo from '@/assets/bitmat_logo.png'
import neopequesLogo from '@/assets/neopeques_logo.png'
import lecoqLogo from '@/assets/lecoq_logo.jpg'
import { useToast } from '@/composables/useToast'
import api from '@/api/api'

const matches = ref([])
const { showToast } = useToast()
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

function formatDate(date) {
  return new Date(date).toLocaleDateString('es-ES')
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
    showToast('Could not load ...', 'error')
  }
}
onMounted(loadHomeData)
</script>

<template>
  <section class="home">
    <section class="home-hero">
      <div class="hero-inner">
        <img src="@/assets/logo_ues.png" alt="Torneo Germans Bisbal" class="hero-logo" />

        <div class="hero-title">
          <span class="hero-kicker"> TORNEO </span>

          <h1>
            GERMANS<br />
            BISBAL
          </h1>

          <p class="hero-slogan">PASSIÓ PEL RUGBY</p>
        </div>

        <div class="quick-nav">
          <RouterLink to="/standings"><i class="fa-solid fa-ranking-star"></i> Standings</RouterLink>

          <RouterLink to="/matches"><i class="fa-solid fa-calendar-days"></i> Matches</RouterLink>

          <RouterLink to="/teams"><i class="fa-solid fa-people-group"></i> Teams</RouterLink>

          <RouterLink to="/rules"><i class="fa-solid fa-book"></i> Rules</RouterLink>
        </div>
      </div>
    </section>

    <div class="app-container">

      <section class="home-section">
        <div class="section-header">
          <h2>Latest Matches</h2>
          <RouterLink to="/matches">View all</RouterLink>
        </div>

        <div class="mini-list">
          <div v-for="match in matches" :key="match.id" class="mini-card">
            <strong> {{ match.localTeam.name }} vs {{ match.visitorTeam.name }} </strong>

            <div class="match-meta">
              <span>
                <i class="fa-solid fa-location-dot"></i>
                {{ match.field.name }}
              </span>

              <div class="match-meta-row">
                <span>
                  <i class="fa-regular fa-clock"></i>
                  {{ match.startTime?.slice(0, 5) }}
                </span>

                <span>
                  <i class="fa-regular fa-calendar"></i>
                  {{ formatDate(match.matchDate) }}
                </span>
              </div>
            </div>
          </div>
        </div>
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
      <section class="poster-section">
        <img
          src="@/assets/cartel_torneo.jpg"
          alt="Torneo Germans Bisbal poster"
          class="poster-image"
        />
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
    </div>
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
  position: relative;
  overflow: hidden;
  width: 100vw;
  margin-left: calc(50% - 50vw);
  margin-right: calc(50% - 50vw);
  text-align: center;
  padding-top: 44px;
  padding-bottom: 36px;
  background: var(--primary);
  color: white;
}

.home-hero::before {
  content: "";
  position: absolute;
  inset: 0;
  opacity: .35;
  background-image:
    repeating-linear-gradient(90deg, rgba(255,255,255,.06) 0 1px, transparent 1px 64px),
    repeating-linear-gradient(0deg, rgba(255,255,255,.06) 0 1px, transparent 1px 64px);
  mask-image: radial-gradient(circle at 50% 0%, black, transparent 75%);
  pointer-events: none;
}

.hero-inner {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 720px;
  margin: 0 auto;
  padding-inline: 24px;
}

.hero-logo {
  width: 84px;
  height: 84px;
  object-fit: contain;
  margin-bottom: 12px;
  filter: drop-shadow(0 4px 10px rgba(0, 0, 0, 0.25));
}

.hero-kicker {
  display: block;
  color: var(--accent);
  font-size: 13px;
  font-weight: 700;
  font-family: var(--font-heading);
  letter-spacing: 3px;
  margin-bottom: 6px;
}

.hero-title h1 {
  margin: 0;
  color: white;
  font-size: 46px;
  line-height: 0.95;
  font-weight: 700;
  font-family: var(--font-heading);
}

.hero-slogan {
  margin-top: 12px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 2px;
}

.quick-nav {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-top: 28px;
  border-bottom: none;
  padding-bottom: 0;
  overflow-x: visible;
}

.quick-nav a {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 999px;
  color: white;
  text-decoration: none;
  font-family: var(--font-heading);
  font-weight: 500;
  font-size: 0.85rem;
  white-space: nowrap;
}

.quick-nav a:hover {
  background: var(--accent);
  border-color: var(--accent);
  color: var(--primary-dark);
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
  margin-top: 2rem;
  color: var(--primary);
  font-size: 20px;
}

.section-header a {
  margin-top: 1rem;
  color: var(--primary-light);
  font-weight: 700;
  text-decoration: none;
  font-size: 14px;
}

.section-header a:hover {
  color: var(--primary);
  font-size: 18px;
}

.match-meta {
  display: flex;
  flex-direction: column;
  gap: 6px;

  color: var(--text-secondary);
  font-size: 0.9rem;
}

.match-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.match-meta i {
  width: 14px;
  color: var(--primary-light);
}

.match-meta-row {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 26px;
  margin-top: 4px;
}

.match-meta-row span {
  display: flex;
  align-items: center;
  gap: 6px;

  color: var(--text-secondary);
  font-size: 0.9rem;
}

.match-meta-row i {
  color: var(--primary-light);
}

.mini-list {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}

.mini-card {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 5px;
  padding: 20px;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  overflow: hidden;
}

.mini-card strong {
  color: var(--primary);
  font-size: 1.3rem;
  line-height: 1.35;
}

.mini-card span {
  color: var(--text-secondary);
  font-size: 0.9rem;
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
  margin-top: 3rem;
  padding: 8px;
  max-width: 1000px;
}

.about-section h2 {
  margin-top: 0;

  color: var(--primary);
}

.about-section p {
  line-height: 1.8;

  color: var(--text-secondary);
}

/* sponsors-section  */

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
  background: #fcfdfd;
}

.sponsor-card.next {
  z-index: 2;
  opacity: 0.55;
  transform: translateX(28%) scale(0.85);
  background: #fcfdfd;
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

@media (min-width: 1024px) {
  .sponsors-section {
    margin-top: 72px;
    margin-bottom: 80px;
  }

  .coverflow-carousel {
    max-width: 920px;
    margin: 32px auto 0;
  }

  .sponsor-stage {
    height: 420px;
  }

  .sponsor-card {
    width: 460px;
    max-width: 460px;
    height: 315px;
  }

  .sponsor-card img {
    max-width: 350px;
    max-height: 180px;
  }

  .sponsor-card.previous {
    transform: translateX(-45%) scale(0.86);
  }

  .sponsor-card.next {
    transform: translateX(45%) scale(0.86);
  }

  .carousel-arrow {
    width: 38px;
    height: 38px;
    font-size: 24px;
  }

  .carousel-arrow.left {
    left: 0;
  }

  .carousel-arrow.right {
    right: 0;
  }

  .mini-list {
    grid-template-columns: repeat(3, 1fr);
    gap: 18px;
  }

  .mini-card {
    min-height: 145px;
    padding: 20px;
  }

  .mini-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 22px rgba(0, 0, 0, 0.12);
  }
}
</style>
