#!/bin/bash

# Script para verificar el funcionamiento del pipeline completo de Wikimedia

echo "🧪 Test del Pipeline Wikimedia - Verificación completa"
echo "======================================================"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para verificar si un comando existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Verificar dependencias
echo -e "${BLUE}🔍 Verificando dependencias...${NC}"
if ! command_exists curl; then
    echo -e "${RED}❌ curl no está instalado${NC}"
    exit 1
fi

# Verificar Docker Compose (tanto docker-compose como docker compose)
if command_exists docker-compose; then
    DOCKER_COMPOSE_CMD="docker-compose"
elif docker compose version >/dev/null 2>&1; then
    DOCKER_COMPOSE_CMD="docker compose"
else
    echo -e "${RED}❌ Docker Compose no está disponible${NC}"
    echo -e "${YELLOW}💡 Instala Docker Compose o usa Docker Desktop${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Dependencias OK${NC}"
echo

# Función para hacer petición HTTP con retry
http_test() {
    local url=$1
    local description=$2
    local max_attempts=10
    local attempt=1
    
    echo -e "${BLUE}🔗 Verificando: $description${NC}"
    echo -e "   URL: $url"
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ $description - OK${NC}"
            return 0
        else
            echo -e "${YELLOW}⏳ Intento $attempt/$max_attempts - Esperando...${NC}"
            sleep 10
            ((attempt++))
        fi
    done
    
    echo -e "${RED}❌ $description - FAILED después de $max_attempts intentos${NC}"
    return 1
}

# Función para verificar servicio de Docker
service_status() {
    local service=$1
    local status=$(docker-compose ps -q $service 2>/dev/null)
    
    if [ -n "$status" ]; then
        local running=$(docker inspect --format='{{.State.Running}}' $status 2>/dev/null)
        if [ "$running" = "true" ]; then
            echo -e "${GREEN}✅ $service - Running${NC}"
            return 0
        else
            echo -e "${RED}❌ $service - Stopped${NC}"
            return 1
        fi
    else
        echo -e "${RED}❌ $service - Not found${NC}"
        return 1
    fi
}

echo -e "${BLUE}🐳 Verificando servicios de Docker...${NC}"
echo "============================================"

# Verificar servicios están corriendo
services=("zookeeper" "kafka" "mongodb" "producer-wikimedia" "transform-wikimedia" "consumer-wikimedia" "kafka-ui")
failed_services=0

for service in "${services[@]}"; do
    if ! service_status $service; then
        ((failed_services++))
    fi
done

if [ $failed_services -gt 0 ]; then
    echo -e "${RED}❌ $failed_services servicios no están corriendo${NC}"
    echo -e "${YELLOW}💡 Ejecuta: docker-compose up --build -d${NC}"
else
    echo -e "${GREEN}✅ Todos los servicios están corriendo${NC}"
fi

echo

# Verificar endpoints HTTP
echo -e "${BLUE}🌐 Verificando endpoints HTTP...${NC}"
echo "=================================="

# Health checks
failed_http=0

if ! http_test "http://localhost:8081/actuator/health" "Producer Health Check"; then
    ((failed_http++))
fi

if ! http_test "http://localhost:8080/actuator/health" "Transform Health Check"; then
    ((failed_http++))
fi

if ! http_test "http://localhost:8090/api/wikimedia-events/actuator/health" "Consumer Health Check"; then
    ((failed_http++))
fi

if ! http_test "http://localhost:8082" "Kafka UI"; then
    ((failed_http++))
fi

echo

# Verificar API de eventos
echo -e "${BLUE}📊 Verificando API de eventos...${NC}"
echo "================================="

events_response=$(curl -s "http://localhost:8090/api/wikimedia-events/events" 2>/dev/null)
if [ $? -eq 0 ]; then
    # Verificar si la respuesta contiene datos
    if echo "$events_response" | grep -q '"data"'; then
        event_count=$(echo "$events_response" | grep -o '"data":\[.*\]' | grep -o '\[.*\]' | grep -o ',' | wc -l)
        event_count=$((event_count + 1))
        if [ "$event_count" -gt 0 ]; then
            echo -e "${GREEN}✅ API de eventos - OK ($event_count eventos encontrados)${NC}"
        else
            echo -e "${YELLOW}⚠️  API de eventos - Responde pero sin datos aún${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️  API de eventos - Responde pero formato inesperado${NC}"
    fi
else
    echo -e "${RED}❌ API de eventos - No responde${NC}"
    ((failed_http++))
fi

echo

# Verificar tópicos de Kafka (usando logs de init)
echo -e "${BLUE}📨 Verificando tópicos de Kafka...${NC}"
echo "==================================="

kafka_logs=$(docker-compose logs kafka-init-topics 2>/dev/null | grep "Tópicos creados" | wc -l)
if [ "$kafka_logs" -gt 0 ]; then
    echo -e "${GREEN}✅ Tópicos de Kafka - Creados correctamente${NC}"
else
    echo -e "${YELLOW}⚠️  Tópicos de Kafka - Verificar logs de kafka-init-topics${NC}"
fi

echo

# Resumen final
echo -e "${BLUE}📋 Resumen de la verificación${NC}"
echo "============================="

total_checks=$((${#services[@]} + 4 + 1)) # servicios + health checks + kafka ui + events api + kafka topics
total_failed=$((failed_services + failed_http))

if [ $total_failed -eq 0 ]; then
    echo -e "${GREEN}🎉 ¡Todos los tests pasaron! El pipeline está funcionando correctamente.${NC}"
    echo
    echo -e "${BLUE}🔗 URLs útiles:${NC}"
    echo "  • API de eventos: http://localhost:8090/api/wikimedia-events/events"
    echo "  • Kafka UI: http://localhost:8082"
    echo "  • Producer: http://localhost:8081/actuator/health"
    echo "  • Transform: http://localhost:8080/actuator/health"
    echo "  • Consumer: http://localhost:8090/api/wikimedia-events/actuator/health"
    echo
    echo -e "${GREEN}✨ ¡El sistema está listo para usar!${NC}"
    exit 0
else
    echo -e "${RED}❌ $total_failed/$total_checks checks fallaron${NC}"
    echo
    echo -e "${YELLOW}🛠️  Pasos para solucionar problemas:${NC}"
    echo "  1. Verificar logs: docker-compose logs [service-name]"
    echo "  2. Reiniciar servicios: docker-compose restart"
    echo "  3. Reconstruir: docker-compose up --build"
    echo "  4. Verificar puertos libres: netstat -tulpn | grep :8080"
    echo
    exit 1
fi
