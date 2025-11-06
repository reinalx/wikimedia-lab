# Wikimedia Stream Event Lab - Fase 1

🎯 **Sistema de procesamiento en tiempo real de eventos de Wikimedia**

Este proyecto implementa un pipeline de procesamiento en tiempo real que consume el feed público de eventos de Wikimedia, aplica filtros y almacena los datos en MongoDB, exponiendo una API REST para consultas.

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Wikimedia      │    │  Producer       │    │  Transform      │    │  Consumer       │
│  Stream         │ -> │  Wikimedia      │ -> │  Wikimedia      │ -> │  Wikimedia      │
│  (External)     │    │  (Port 8081)    │    │  (Port 8080)    │    │  (Port 8090)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
                                 |                       |                       |
                                 v                       v                       v
                         ┌───────────────┐    ┌───────────────┐    ┌───────────────┐
                         │ wikimedia.    │    │ wikimedia.    │    │   MongoDB     │
                         │ raw.events    │    │ filtered.     │    │   Database    │
                         │ (Kafka Topic) │    │ events        │    │               │
                         └───────────────┘    │ (Kafka Topic) │    └───────────────┘
                                              └───────────────┘
```

## 📦 Componentes

### 🔄 Producer Wikimedia (Puerto 8081)
- **Responsabilidad**: Consume el stream público de Wikimedia y publica eventos sin modificar
- **Tópico destino**: `wikimedia.raw.events`
- **Tecnología**: Spring Boot 3 + WebClient reactivo

### ⚙️ Transform Wikimedia (Puerto 8080)
- **Responsabilidad**: Procesa eventos del tópico raw aplicando filtros de negocio
- **Tópico origen**: `wikimedia.raw.events`
- **Tópico destino**: `wikimedia.filtered.events`
- **Filtros aplicados**: Solo ediciones en español, excluir bots, etc.

### 💾 Consumer Wikimedia (Puerto 8090)
- **Responsabilidad**: Persiste eventos filtrados en MongoDB y expone API REST
- **Tópico origen**: `wikimedia.filtered.events`
- **Base de datos**: MongoDB
- **API**: `/api/wikimedia-events/events`

## 🚀 Inicio Rápido

### Prerrequisitos
- Docker y Docker Compose
- Java 21 (para desarrollo local)
- Maven 3.9+ (para desarrollo local)

### 🎯 Ejecutar todo el sistema

```bash
# Clonar el repositorio
git clone <repository-url>
cd wikimedia-lab

# IMPORTANTE para Mac con Apple Silicon (M1/M2/M3):
# Limpiar imágenes problemáticas anteriores si las hay
docker-compose down --rmi local
docker system prune -f

# Construir y ejecutar todos los servicios
docker-compose up --build

# Para ejecutar en segundo plano
docker-compose up --build -d
```

#### 🍎 **Nota especial para Apple Silicon (M1/M2/M3)**
Este proyecto está optimizado para funcionar en Mac con chips Apple Silicon. Si experimentas problemas de construcción, asegúrate de:
- Usar Docker Desktop actualizado (versión 4.x+)
- Tener suficiente memoria asignada a Docker (mínimo 4GB recomendado)

### ⏱️ Tiempo de inicio esperado
- **Infraestructura** (Kafka, MongoDB): ~30-45 segundos
- **Servicios Java**: ~60-90 segundos adicionales
- **Sistema completo**: ~2-3 minutos

## 🔍 Verificar funcionamiento

### 1. Verificar servicios están corriendo
```bash
docker-compose ps
```

### 2. Verificar logs de cada servicio
```bash
# Producer (debería mostrar conexión al stream de Wikimedia)
docker-compose logs producer-wikimedia

# Transform (debería mostrar procesamiento de eventos)
docker-compose logs transform-wikimedia

# Consumer (debería mostrar persistencia en MongoDB)
docker-compose logs consumer-wikimedia
```

### 3. Verificar tópicos de Kafka
Acceder a Kafka UI: http://localhost:8082
- Deberías ver los tópicos: `wikimedia.raw.events` y `wikimedia.filtered.events`
- Verificar que hay mensajes siendo procesados

### 4. Verificar API REST
```bash
# Consultar eventos almacenados
curl http://localhost:8090/api/wikimedia-events/events

# Health check del consumer
curl http://localhost:8090/api/wikimedia-events/actuator/health
```

### 5. Verificar MongoDB
```bash
# Conectar a MongoDB
docker exec -it mongodb mongosh -u root -p example --authenticationDatabase admin

# En el shell de MongoDB
use wikimedia
db.events.countDocuments()
db.events.findOne()
```

## 📊 Monitoreo y Debug

### Puertos expuestos
| Servicio | Puerto | Descripción |
|----------|---------|-------------|
| Producer Wikimedia | 8081 | Servicio productor |
| Transform Wikimedia | 8080 | Servicio de transformación |
| Consumer Wikimedia | 8090 | Servicio consumidor + API |
| Kafka UI | 8082 | Interfaz web para Kafka |
| MongoDB | 27017 | Base de datos |
| Kafka | 9092 | Broker Kafka |
| Zookeeper | 2181 | Coordinación Kafka |

### URLs útiles
- **API de eventos**: http://localhost:8090/api/wikimedia-events/events
- **Kafka UI**: http://localhost:8082
- **Health checks**:
  - Producer: http://localhost:8081/actuator/health
  - Transform: http://localhost:8080/actuator/health
  - Consumer: http://localhost:8090/api/wikimedia-events/actuator/health

## 🛠️ Desarrollo local

### Ejecutar servicios individualmente
```bash
# Solo infraestructura
docker-compose up -d zookeeper kafka mongodb kafka-init-topics

# Ejecutar un servicio Java localmente
cd producer-wikimedia
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Variables de entorno para desarrollo local
```bash
# Producer
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export WIKIMEDIA_STREAM_URL=https://stream.wikimedia.org/v2/stream/recentchange

# Transform
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export APP_KAFKA_TOPICS_RAW=wikimedia.raw.events
export APP_KAFKA_TOPICS_FILTERED=wikimedia.filtered.events

# Consumer
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export SPRING_DATA_MONGODB_URI=mongodb://root:example@localhost:27017/wikimedia?authSource=admin
export APP_KAFKA_TOPICS_FILTERED=wikimedia.filtered.events
```

## 🧪 Testing

### Verificar el flujo completo
1. **Productor funcionando**: Logs muestran conexión a Wikimedia stream
2. **Mensajes en raw topic**: Kafka UI muestra mensajes en `wikimedia.raw.events`
3. **Transform procesando**: Logs muestran filtrado de eventos
4. **Mensajes en filtered topic**: Kafka UI muestra mensajes en `wikimedia.filtered.events`
5. **Persistencia en MongoDB**: Consulta directa a la base de datos
6. **API disponible**: `curl` a los endpoints REST

### Comandos de test
```bash
# Test básico del pipeline
./test-pipeline.sh

# O manualmente:
curl -s http://localhost:8090/api/wikimedia-events/events | jq '.data | length'
```

## 🚨 Solución de problemas

### Servicios no inician
```bash
# Verificar logs
docker-compose logs [service-name]

# Reiniciar un servicio específico
docker-compose restart producer-wikimedia
```

### Sin datos en MongoDB
1. Verificar que el producer está conectado al stream de Wikimedia
2. Verificar que hay mensajes en los tópicos de Kafka
3. Verificar que el transform está procesando (filtros no muy restrictivos)
4. Verificar conexión del consumer a MongoDB

### Problemas de conectividad
```bash
# Verificar red de Docker
docker network ls
docker network inspect wikimedia-lab_default

# Verificar conectividad entre contenedores
docker exec producer-wikimedia ping kafka
docker exec consumer-wikimedia ping mongodb
```

## 🏁 Parar el sistema

```bash
# Parar todos los servicios
docker-compose down

# Parar y eliminar volúmenes (CUIDADO: elimina datos)
docker-compose down -v

# Limpiar imágenes construidas
docker-compose down --rmi local
```
## 🤝 Contribuir

1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/amazing-feature`)
3. Commit cambios (`git commit -m 'Add amazing feature'`)
4. Push a la rama (`git push origin feature/amazing-feature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.
