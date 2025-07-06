#!/bin/bash

# Script per caricare le immagini Docker in Minikube

images=(
  "eventhub/user-microservice:latest"
  "eventhub/mail-microservice:latest"
  "eventhub/agenda-microservice:latest"
  "eventhub/ticket-microservice:latest"
  "eventhub/event-microservice:latest"
  "eventhub/gateway:v1"
)

for image in "${images[@]}"; do
  echo "📦 Caricamento immagine: $image"
  minikube image load "$image"
done

echo "✅ Tutte le immagini sono state caricate in Minikube."
