#!/bin/sh
# Fills in the backend host/port placeholders in nginx.conf at container
# startup, so the same image works both in local docker-compose (where the
# backend service is reachable as "backend") and in Railway (where it's a
# different private-network hostname, provided via env vars).
set -e

# nginx's resolver directive needs IPv6 addresses wrapped in brackets;
# IPv4 ones (e.g. Docker's local resolver, 127.0.0.11) are used as-is.
RESOLVER=$(awk '/^nameserver/ { print $2; exit }' /etc/resolv.conf)
case "$RESOLVER" in
  *:*) RESOLVER="[$RESOLVER]" ;;
esac

sed -i "s|__BACKEND_HOST__|${BACKEND_HOST}|g; s|__BACKEND_PORT__|${BACKEND_PORT}|g; s|__RESOLVER__|${RESOLVER}|g" /etc/nginx/conf.d/default.conf

exec nginx -g 'daemon off;'
