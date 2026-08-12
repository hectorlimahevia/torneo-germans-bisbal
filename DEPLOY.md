# Despliegue en producción

El proyecto ya está dockerizado (`backend/Dockerfile`, `frontend/Dockerfile`, `docker-compose.yml`), así que puede desplegarse en cualquier proveedor que soporte contenedores. La opción recomendada es **Railway**, porque permite tener MySQL + backend + frontend en un mismo proyecto sin configuración adicional.

## Opción recomendada: Railway

1. Crea una cuenta en [railway.com](https://railway.com/) y un nuevo proyecto.
2. **Base de datos**: añade un plugin "MySQL" desde la plantilla de Railway. Railway generará automáticamente variables `MYSQLHOST`, `MYSQLPORT`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQLDATABASE`.
3. **Backend**: "New Service" → "Deploy from GitHub repo" → selecciona `torneo-germans-bisbal` → en "Settings" fija el **Root Directory** en `backend` (Railway detectará el `Dockerfile` automáticamente). Variables de entorno a añadir:
   - `SPRING_DATASOURCE_URL` = `jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}`
   - `SPRING_DATASOURCE_USERNAME` = `${{MySQL.MYSQLUSER}}`
   - `SPRING_DATASOURCE_PASSWORD` = `${{MySQL.MYSQLPASSWORD}}`
   - `OPENAI_API_KEY` = (opcional)
   - `APP_CORS_ALLOWED_ORIGINS` = la URL pública que Railway asigne al frontend (la sabrás tras el paso 4; puedes actualizarla después)
   - `APP_JWT_SECRET` = una clave aleatoria propia de este entorno (genera una con `openssl rand -base64 48`, distinta de la que uses en local) — **obligatoria**, si no se define el backend arranca con un valor de aviso no apto para producción.
   - `APP_COOKIE_SECURE` = `true` (Railway sirve por HTTPS, así que es el valor correcto; es también el default si no la defines).
   - Railway inyecta `PORT` automáticamente; el backend ya lo respeta (`server.port: ${PORT:8080}`).
4. **Frontend**: "New Service" → mismo repo → **Root Directory** en `frontend`. Antes de desplegar, cambia en `frontend/docker/nginx.conf` la línea `proxy_pass http://backend:8080/api/;` por la URL pública del servicio backend de Railway (te la da Railway al desplegarlo), o usa una variable de red interna de Railway si prefieres mantenerlas en el mismo proyecto privado.
5. En cada servicio, pulsa "Generate Domain" para obtener una URL pública tipo `*.up.railway.app`.
6. Actualiza `APP_CORS_ALLOWED_ORIGINS` en el backend con la URL final del frontend y vuelve a desplegar.

Cada push a la rama configurada en Railway (por defecto, la rama por defecto del repo) despliega automáticamente.

## Alternativa: un VPS propio con Docker Compose

Si prefieres un servidor propio (DigitalOcean, Hetzner, etc.) con Docker instalado:

```bash
git clone https://github.com/hectorlimahevia/torneo-germans-bisbal.git
cd torneo-germans-bisbal
cp .env.example .env   # edita las contraseñas
docker compose up -d --build
```

Pon un proxy inverso (Caddy o Nginx) delante para servir con dominio propio y HTTPS, apuntando al puerto `8081` (frontend).

## Alternativa: hosting separado (Vercel/Netlify + Render)

Si prefieres el frontend y el backend en proveedores distintos:

- **Frontend** (Vercel/Netlify): build command `npm run build`, output `dist`. Define `VITE_API_BASE_URL` con la URL pública del backend (por ejemplo `https://torneo-api.onrender.com`).
- **Backend** (Render/Railway): despliega `backend/Dockerfile` igual que en el paso 3 anterior.
- Como frontend y backend quedan en dominios distintos, el navegador sí hace peticiones cross-origin: añade la URL exacta del frontend a `APP_CORS_ALLOWED_ORIGINS` en el backend.

## Checklist antes de desplegar

- [ ] `OPENAI_API_KEY` configurado si quieres el asistente IA activo.
- [ ] `APP_CORS_ALLOWED_ORIGINS` incluye la URL pública real del frontend.
- [ ] Contraseña de MySQL cambiada respecto al valor por defecto de `.env.example`.
- [ ] `APP_JWT_SECRET` generado de nuevo para este entorno (no reutilizar el de desarrollo local).
- [ ] `APP_COOKIE_SECURE=true` y el sitio se sirve por HTTPS (si no hay HTTPS delante, la cookie de sesión persistente no se enviará nunca).
- [ ] Si frontend y backend quedan en dominios distintos (opción "hosting separado"), revisar que la cookie de refresh token (`SameSite=Lax`) siga funcionando entre esos dos orígenes.
- [ ] `docker compose up --build` probado en local antes de desplegar.
