#!/bin/bash

# Abilita uscita in caso di errori
set -e

echo "Creazione rete microservices-network..."
docker network inspect microservices-network >/dev/null 2>&1 || \
  docker network create microservices-network

echo "Avvio utenti-service..."
docker compose -f microservizi/utenti-service/docker-compose.yml up -d --build

# Avvio dei microservizi
echo "Avvio mail-service..."
docker compose -f microservizi/mail_service/docker-compose.yml up -d --build

echo "Avvio agenda-service..."
docker compose -f microservizi/agenda_service/docker-compose.yml up -d --build

echo "Avvio ticket-service..."
docker compose -f microservizi/ticket_service/docker-compose.yml up -d --build

echo "Avvio event-service..."
docker compose -f microservizi/event_service/docker-compose.yml up -d --build

echo "Avvio gateway..."
docker compose -f gateway/docker-compose.yml up -d --build

echo "Servizi sono avviati!"
