# Progetto di TAASS - eventhub be


## Step 1: Eseguire il build

Eseguire lo script per creare le immagini dei microservizi:

```bash
    ./build-all.sh
```

Caricare le immagini in minikube:

```bash
    ./load-all-images.sh
```

## Step 2: Applicare i manifest in ordine

```bash
    minikube start
    minikube dashboard
```

Crea lo spazio dei nomi:

```bash
    kubectl apply -f k8s/namespace.yml
```

Applicare i manifest in ordine:

```bash
    kubectl apply -f k8s/configmap/
    kubectl apply -f k8s/secrets/
    kubectl apply -f k8s/pvc/
    kubectl apply -f k8s/service/
    kubectl apply -f k8s/deployment/
```

## Step 3: Far partire il gateway su minikube

```bash
    kubectl port-forward nome-pod-gateway-service 8082:8082 -n eventhub
```
