<template>
  <footer class="app-footer">
    <div class="footer-text">
      <p class="footer-brand">Torneo Germans Bisbal <span>UES</span></p>
      <p class="footer-copy">© 2026 · Passió pel rugby</p>
    </div>

    <div class="social-links">
      <a href="#" aria-label="Instagram">
        <i class="fa-brands fa-instagram"></i>
      </a>

      <a href="#" aria-label="Facebook">
        <i class="fa-brands fa-facebook"></i>
      </a>

      <a href="#" aria-label="X">
        <i class="fa-brands fa-x-twitter"></i>
      </a>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 16px;
  text-align: center;
  border-top: 1px solid var(--border);
  color: #e2e5e7;
  background: var(--primary-dark);
}

.footer-text {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.footer-brand {
  margin: 0 0 4px;
  font-family: var(--font-heading);
  font-weight: 600;
  font-size: 1.05rem;
  letter-spacing: 0.03em;
}

.footer-copy {
  margin: 0;
}

.footer-brand span {
  color: var(--accent);
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 24px;
}

@media (min-width: 768px) {
  .app-footer {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    text-align: left;
    padding-inline: 32px;
  }

  .footer-text {
    flex-direction: row;
    align-items: baseline;
    gap: 10px;
  }

  .footer-brand {
    margin: 0;
  }
}

.social-links a {
  color: #e2e5e7;

  font-size: 24px;

  transition:
    color 0.3s ease,
    transform 0.3s ease;
}

.social-links a:hover {
  color: var(--accent);
  transform: translateY(-4px) scale(1.2);
  text-shadow: 0 0 12px rgba(217, 154, 43, 0.45);
}

.footer-tech {
  font-size: 0.85rem;
}
</style>
