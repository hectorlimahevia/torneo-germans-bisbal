# Puesta en marcha en local

Guía para arrancar el proyecto en un ordenador nuevo (backend Spring Boot + frontend Vue + MySQL).

## Opción rápida: Docker Compose

Si tienes Docker Desktop instalado, esta es la forma más simple: levanta MySQL, backend y frontend con un solo comando, sin instalar Java, Maven ni Node.

1. Instala [Docker Desktop](https://www.docker.com/products/docker-desktop/).
2. En la raíz del proyecto:
   ```bash
   cp .env.example .env
   docker compose up --build
   ```
3. Abre `http://localhost:8081` (frontend) — la API queda accesible en `http://localhost:8080`.

La primera vez tardará varios minutos en construir las imágenes. Las siguientes veces será mucho más rápido.

## Opción manual (sin Docker)

### Requisitos

- **Java 25 (JDK)** — el proyecto usa Spring Boot 4 / Java 25.
- **Node.js 20.19+ o 22.12+** y npm.
- **MySQL 8** en local (o accesible remotamente).
- Git.

### 1. Base de datos

Crea la base de datos vacía (Hibernate se encarga de las tablas con `ddl-auto: update`):

```sql
CREATE DATABASE torneo_germans_bisbal_db;
```

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

Por defecto se conecta a `jdbc:mysql://localhost:3306/torneo_germans_bisbal_db` con usuario `root` y sin contraseña. Para usar otras credenciales, exporta variables de entorno antes de arrancar (o crea un `.env`/perfil local):

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/torneo_germans_bisbal_db
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=tu_password
export OPENAI_API_KEY=            # opcional, solo si quieres el asistente IA
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### 3. Frontend

En otra terminal:

```bash
cd frontend
npm install
npm run dev
```

Se abre en `http://localhost:5173` y ya apunta al backend en `http://localhost:8080` (configurado en `src/api/api.js` / `.env`).

### Notas para este equipo nuevo

Como el repo se acaba de clonar en esta máquina, recuerda:
- Instalar Java 25 y Node antes de nada (comprueba con `java -version` y `node -v`).
- El primer `./mvnw spring-boot:run` descargará Maven y todas las dependencias — necesita conexión a internet la primera vez.
- El primer `npm install` puede tardar 1-2 minutos.
