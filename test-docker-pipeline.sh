#!/bin/bash

# Script to test the Wikimedia pipeline with Docker Compose
# Make sure Docker Desktop or Podman is running before executing

echo "🚀 Starting Wikimedia pipeline..."
echo "==============================================="

# Clean previous containers and volumes
echo "🧹 Cleaning previous containers and volumes..."
docker compose down -v 2>/dev/null || echo "No containers were running"

# Remove project images to force a full rebuild
echo "🔄 Removing project images..."
docker images | grep wikimedia-lab | awk '{print $3}' | xargs docker rmi -f 2>/dev/null || echo "No previous images found"

# Build and start services
echo "🔨 Building and starting services..."
docker compose up --build

# If you want to run in the background, use:
# docker compose up --build -d

echo "✅ Pipeline started"
echo ""
echo "📊 Available services:"
echo "- Producer Wikimedia: http://localhost:8081/actuator/health"
echo "- Transform Wikimedia: http://localhost:8080/actuator/health"
echo "- Consumer Wikimedia: http://localhost:8090/api/wikimedia-events/actuator/health"
echo "- Kafka UI: http://localhost:8082"
echo "- MongoDB: localhost:27017 (root/example)"
