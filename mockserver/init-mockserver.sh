#!/bin/sh
# init-mockserver.sh
# Aguarda MockServer DICT e SPI responderem.
# Keycloak agora é um container real com healthcheck próprio.

DICT_URL="http://mockserver-dict:1080"
SPI_URL="http://mockserver-spi:1080"

wait_for() {
  local name=$1
  local url=$2
  echo "Aguardando $name ..."
  until curl -sf "$url/mockserver/status" > /dev/null 2>&1; do
    sleep 2
  done
  echo "$name pronto."
}

wait_for "MockServer DICT" "$DICT_URL"
wait_for "MockServer SPI"  "$SPI_URL"

echo "MockServers prontos!"
