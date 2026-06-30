<script setup>
import uesLogo from '@/assets/logo_ues.png'

const props = defineProps({
  title: {
    type: String,
    required: true,
  },

  subtitle: {
    type: String,
    default: '',
  },
})
</script>

<template>
  <section class="auth-layout">
    <div class="auth-brand">
      <img
        :src="uesLogo"
        alt="UES logo"
        class="auth-logo"
      >

      <h1>{{ props.title }}</h1>

      <p v-if="props.subtitle">
        {{ props.subtitle }}
      </p>
    </div>

    <div class="auth-card">
      <slot />
    </div>

   </section>
</template>

<style scoped>
.auth-layout {
  position: relative;
  min-height: calc(100vh - 6rem);
  display: flex;
  flex-direction: column;
  justify-content: center;

  padding: 24px 22px;
  background-image: url('@/assets/fondo_login.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.auth-brand {
  position: relative;
  z-index: 1;
  text-align: center;
}

.auth-logo {
  width: 130px;
  margin-bottom: 12px;
}

.auth-brand h1 {
  margin: 0;
  color: var(--primary);
  font-size: 2rem;
  font-weight: 900;
}

.auth-brand p {
  margin: 8px 0 0;
  color: var(--text-secondary);
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  font-size: 0.75rem;
}

.auth-card {
  position: relative;
  z-index: 1;
  background: transparent;
  padding: 20px;

}

@media (min-width: 992px) {
  .auth-layout {
    align-items: center;
    padding: 60px 24px;
  }

  .auth-brand {
    max-width: 520px;
  }

  .auth-card {
    width: 100%;
    max-width: 520px;
    padding: 32px;
  }
}

@media (max-width: 991px) {
  .auth-card {
    padding: 20px 0;
  }
}

</style>
