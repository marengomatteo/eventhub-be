#!/bin/bash

# Script per caricare tutte le immagini su Minikube e deployare i microservizi
echo "🚀 Inizio deployment dei microservizi su Minikube"

# Verifica che Minikube sia in esecuzione
if ! minikube status > /dev/null 2>&1; then
    echo "❌ Minikube non è in esecuzione. Avvialo con 'minikube start'"
    exit 1
fi

echo "✅ Minikube è in esecuzione"

# Array con i nomi delle immagini Docker (modifica questi nomi con i tuoi effettivi)
IMAGES=(
    "utenti-service-user-service:latest"
    # "mail_service-mail-service:latest"
    # "agenda_service-agenda-service:latest"
    # "event_service-event-service:latest"
    # "ticket_service-ticket-service:latest"
    # "gateway-gateway:latest"
)

# echo "📦 Caricamento delle immagini Docker su Minikube..."

# Carica ogni immagine su Minikube
for image in "${IMAGES[@]}"; do
    echo "🔄 Caricamento $image..."
    if docker images -q "$image" > /dev/null 2>&1; then
        minikube image load "$image"
        if [ $? -eq 0 ]; then
            echo "✅ $image caricata con successo"
        else
            echo "❌ Errore nel caricamento di $image"
            exit 1
        fi
    else
        echo "⚠️  Immagine $image non trovata in Docker locale"
        echo "   Assicurati di aver buildato l'immagine con: docker build -t $image ."
        exit 1
    fi
done

echo ""
echo "🔍 Verifica delle immagini caricate su Minikube:"
minikube image ls | grep -E "(agenda|event|gateway|mail|ticket|utenti)"

echo ""
echo "🚀 Applicazione dei file di deployment e service..."

# Array con tutti i file YAML da applicare
YAML_FILES=(
    # "k8s/agenda-deployment.yaml"
    # "k8s/agenda-service.yaml"
    # "k8s/event-deployment.yaml"
    # "k8s/event-service.yaml"
    # "k8s/gateway-deployment.yaml"
    # "k8s/gateway-service.yaml"
    # "k8s/mail-deployment.yaml"
    # "k8s/mail-service.yaml"
    # "k8s/ticket-deployment.yaml"
    # "k8s/ticket-service.yaml"
    "k8s/utenti-deployment.yaml"
    "k8s/utenti-service.yaml"
)

# Applica ogni file YAML
for yaml_file in "${YAML_FILES[@]}"; do
    if [ -f "$yaml_file" ]; then
        echo "📋 Applicando $yaml_file..."
        kubectl apply -f "$yaml_file"
        if [ $? -eq 0 ]; then
            echo "✅ $yaml_file applicato con successo"
        else
            echo "❌ Errore nell'applicazione di $yaml_file"
            exit 1
        fi
    else
        echo "⚠️  File $yaml_file non trovato"
        exit 1
    fi
done
