#!/bin/sh
# Fills in the backend host/port placeholders in nginx.conf at container
# startup, so the same image works both in local docker-compose (where the
# backend service is reachable as "backend") and in Railway (where it's a
# different private-network hostname, provided via env vars).
set -e

sed -i "s|__BACKEND_HOST__|${BACKEND_HOST}|g; s|__BACKEND_PORT__|${BACKEND_PORT}|g" /etc/nginx/conf.d/default.conf

exec nginx -g 'daemon off;'
