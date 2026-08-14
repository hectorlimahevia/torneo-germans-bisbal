# Despliegue en producción

El proyecto ya está dockerizado (`backend/Dockerfile`, `frontend/Dockerfile`, `docker-compose.yml`), así que puede desplegarse en cualquier proveedor que soporte contenedores. La opción recomendada es **Railway**, porque permite tener MySQL + backend + frontend en un mismo proyecto sin configuración adicional.

## Opción recomendada: Railway

Sigue este orden exacto — crear los servicios en otro orden, o usar el botón de "conectar variable" automático de Railway, es la causa más común de que el backend no arranque.

1. Crea una cuenta en [railway.com](https://railway.com/) y un **nuevo proyecto**.
2. **Base de datos primero**: `+ New` → `Database` → `Add MySQL` (la plantilla oficial, no un servicio genérico). Espera a que se ponga **"Online"** antes de seguir.
3. **Backend**: `+ New` → `GitHub Repo` → selecciona `torneo-germans-bisbal` → en **Settings → Source** fija el **Root Directory** en `backend`. Antes de dejar que despliegue, ve a **Variables** y añade estas (usa el editor de texto plano — botón **"Raw Editor"** — y pega el bloque entero; evita el asistente "Add a Variable Reference", que a veces crea variables con el nombre equivocado):

   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}
   SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
   SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
   APP_JWT_SECRET=
   APP_COOKIE_SECURE=true
   APP_CORS_ALLOWED_ORIGINS=
   OPENAI_API_KEY=
   ```

   - `APP_JWT_SECRET`: genera una clave con `openssl rand -base64 48` — **obligatoria**, distinta de la de tu `.env` local.
   - `APP_CORS_ALLOWED_ORIGINS`: déjala vacía por ahora, la rellenas en el paso 6.
   - Railway inyecta `PORT` automáticamente; el backend ya lo respeta (`server.port: ${PORT:8080}`).
   - Los nombres `SPRING_DATASOURCE_*` deben ser exactos — es lo único que lee Spring Boot. Si Railway te sugiere variables llamadas `MYSQLUSER`/`MYSQLPASSWORD` sueltas (sin el prefijo `SPRING_DATASOURCE_`), no sirven, bórralas.
4. **Verifica el backend antes de seguir**: pestaña Deployments → espera a "Active" → revisa los logs y confirma la línea `Started TorneoGermansBisbalApiApplication`. No continúes al frontend hasta ver esto — así sabes en qué capa está el problema si algo falla.
5. **Frontend**: `+ New` → `GitHub Repo` → mismo repo → **Root Directory** en `frontend`. En **Variables**, añade:

   ```
   BACKEND_HOST=${{backend.RAILWAY_PRIVATE_DOMAIN}}
   BACKEND_PORT=8080
   ```

   (sustituye `backend` por el nombre exacto que le hayas puesto a tu servicio de backend en Railway si es distinto). El `nginx.conf` de la imagen ya está preparado para leer estas dos variables al arrancar — no hace falta tocar ningún archivo a mano.
6. En cada servicio (backend y frontend), pestaña **Settings → Networking → Generate Domain**, para obtener una URL pública tipo `*.up.railway.app`.
7. Copia la URL pública del **frontend** y ponla en `APP_CORS_ALLOWED_ORIGINS` del **backend** (Variables → editar). Guarda; el backend se redespliega solo.

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
