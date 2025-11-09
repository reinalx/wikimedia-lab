# Wikimedia Stream Event Lab - Phase 1

🎯 **Real-time Wikimedia event processing system**

This project implements a real-time processing pipeline that consumes the public Wikimedia event feed, applies filters, and stores data in MongoDB, exposing a REST API for querying.

## 🏗️ Architecture

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

## 📦 Components

### 🔄 Producer Wikimedia (Port 8081)
- **Responsibility**: Consumes public Wikimedia stream and publishes unmodified events
- **Target Topic**: `wikimedia.raw.events`
- **Technology**: Spring Boot 3 + Reactive WebClient

### ⚙️ Transform Wikimedia (Port 8080)
- **Responsibility**: Processes events from raw topic applying business filters
- **Source Topic**: `wikimedia.raw.events`
- **Target Topic**: `wikimedia.filtered.events`
- **Applied Filters**: Only Spanish edits, exclude bots, etc.

### 💾 Consumer Wikimedia (Port 8090)
- **Responsibility**: Persists filtered events to MongoDB and exposes REST API
- **Source Topic**: `wikimedia.filtered.events`
- **Database**: MongoDB
- **Available APIs**:
  - **Events API**: `/api/wikimedia-events/v1/events` - Retrieve wikimedia events with pagination
  - **Analysis API**: `/api/wikimedia-events/v1/analysis` - Analysis of wikimedia event information
  - **Users API**: `/api/wikimedia-events/v1/users/login` - User authentication (login with JWT)
  - **Health Check**: `/api/wikimedia-events/actuator/health`

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 21 (for local development)
- Maven 3.9+ (for local development)

### 🎯 Run the Complete System

```bash
# Clone the repository
git clone <repository-url>
cd wikimedia-lab

# Use the automated test script (recommended)
./test-docker-pipeline.sh

# Or manually:
docker compose up --build
```

### ⚡ Automated Pipeline Script

The `test-docker-pipeline.sh` script provides a complete solution:

```bash
# Make script executable (if needed)
chmod +x test-docker-pipeline.sh

# Run the complete pipeline
./test-docker-pipeline.sh
```

**What the script does:**
1. 🧹 Cleans previous containers and volumes
2. 🔄 Removes old project images to force complete rebuild
3. 🔨 Builds and starts all services with health checks
4. 📊 Shows available service endpoints

#### 🍎 **Special Note for Apple Silicon (M1/M2/M3)**
This project is optimized to work on Mac with Apple Silicon chips. The Dockerfiles use `amazoncorretto:21-alpine` images which are compatible with ARM64 architecture.

### ⏱️ Expected Startup Time
- **Infrastructure** (Kafka, MongoDB): ~30-45 seconds
- **Java Services**: ~60-90 seconds additional
- **Complete System**: ~2-3 minutes

## 🔍 Verify Operation

### 1. Check All Services Are Running
```bash
docker compose ps
```

### 2. Health Check Endpoints
```bash
# Producer health
curl http://localhost:8081/actuator/health

# Transform health (fixed with web starter)
curl http://localhost:8080/actuator/health

# Consumer health
curl http://localhost:8090/api/wikimedia-events/actuator/health
```

### 3. Check Service Logs
```bash
# Producer (should show Wikimedia stream connection)
docker compose logs producer-wikimedia

# Transform (should show event processing)
docker compose logs transform-wikimedia

# Consumer (should show MongoDB persistence)
docker compose logs consumer-wikimedia
```

### 4. Verify Kafka Topics
Access Kafka UI: http://localhost:8082
- You should see topics: `wikimedia.raw.events` and `wikimedia.filtered.events`
- Verify messages are being processed

### 5. Verify REST API
```bash
# Query stored events (with pagination)
curl http://localhost:8090/api/wikimedia-events/v1/events

# Query with pagination parameters
curl "http://localhost:8090/api/wikimedia-events/v1/events?page=0&size=10"

# Check event count
curl -s http://localhost:8090/api/wikimedia-events/v1/events | jq '.data | length'

# Analysis endpoint
curl http://localhost:8090/api/wikimedia-events/v1/analysis
```

### 6. User Authentication (Adding Users)

The Consumer Wikimedia service includes user authentication functionality. To add and authenticate users:

#### User Login
```bash
# Login with user credentials (returns JWT token)
curl -X POST http://localhost:8090/api/wikimedia-events/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

#### Using JWT Token for Authenticated Requests
```bash
# First, get the JWT token from login
TOKEN=$(curl -X POST http://localhost:8090/api/wikimedia-events/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}' \
  -s | jq -r '.token')

# Use the token for authenticated requests
curl http://localhost:8090/api/wikimedia-events/v1/events \
  -H "Authorization: Bearer $TOKEN"

curl http://localhost:8090/api/wikimedia-events/v1/analysis \
  -H "Authorization: Bearer $TOKEN"
```

**Note**: The current implementation includes JWT-based authentication. You may need to implement user registration endpoints or configure default users in the application for the login functionality to work properly.

### 7. Verify MongoDB
```bash
# Connect to MongoDB
docker exec -it mongodb mongosh -u root -p example --authenticationDatabase admin

# In MongoDB shell
use wikimedia
db.events.countDocuments()
db.events.findOne()
```

## 📊 Monitoring and Debug

### Exposed Ports
| Service | Port | Description |
|---------|------|-------------|
| Producer Wikimedia | 8081 | Producer service + health checks |
| Transform Wikimedia | 8080 | Transform service + health checks |
| Consumer Wikimedia | 8090 | Consumer service + REST API |
| Kafka UI | 8082 | Web interface for Kafka |
| MongoDB | 27017 | Database |
| Kafka | 9092 | Kafka broker |
| Zookeeper | 2181 | Kafka coordination |

### Useful URLs
- **Consumer APIs**:
  - **Events API**: http://localhost:8090/api/wikimedia-events/v1/events
  - **Analysis API**: http://localhost:8090/api/wikimedia-events/v1/analysis
  - **User Login**: http://localhost:8090/api/wikimedia-events/v1/users/login
- **Monitoring**:
  - **Kafka UI**: http://localhost:8082
- **Health Checks**:
  - Producer: http://localhost:8081/actuator/health
  - Transform: http://localhost:8080/actuator/health
  - Consumer: http://localhost:8090/api/wikimedia-events/actuator/health

## 🛠️ Local Development

### Run Services Individually
```bash
# Infrastructure only
docker compose up -d zookeeper kafka mongodb kafka-init-topics

# Run a Java service locally
cd producer-wikimedia
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Environment Variables for Local Development
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

### Verify Complete Flow
1. **Producer working**: Logs show Wikimedia stream connection
2. **Messages in raw topic**: Kafka UI shows messages in `wikimedia.raw.events`
3. **Transform processing**: Logs show event filtering
4. **Messages in filtered topic**: Kafka UI shows messages in `wikimedia.filtered.events`
5. **MongoDB persistence**: Direct database query
6. **API available**: `curl` to REST endpoints

### Test Commands
```bash
# Complete pipeline test
./test-docker-pipeline.sh

# Manual API tests
curl -s http://localhost:8090/api/wikimedia-events/v1/events | jq '.data | length'
curl http://localhost:8090/api/wikimedia-events/v1/analysis
curl -X POST http://localhost:8090/api/wikimedia-events/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'

# Health check all services
curl http://localhost:8081/actuator/health && \
curl http://localhost:8080/actuator/health && \
curl http://localhost:8090/api/wikimedia-events/actuator/health
```

## 🚨 Troubleshooting

### Services Don't Start
```bash
# Check logs
docker compose logs [service-name]

# Restart specific service
docker compose restart producer-wikimedia

# Force complete rebuild
./test-docker-pipeline.sh
```

### Health Check Failures
- **Transform service "unhealthy"**: Fixed with `spring-boot-starter-web` dependency
- **Long startup times**: Health checks now have 90s start period with 5 retries
- **Empty replies**: Ensure actuator endpoints are properly configured

### No Data in MongoDB
1. Verify producer is connected to Wikimedia stream
2. Check messages exist in Kafka topics
3. Verify transform is processing (filters not too restrictive)
4. Check consumer MongoDB connection

### Connectivity Issues
```bash
# Check Docker network
docker network ls
docker network inspect wikimedia-lab_default

# Test container connectivity
docker exec producer-wikimedia ping kafka
docker exec consumer-wikimedia ping mongodb
```

### Apple Silicon (M1/M2/M3) Issues
```bash
# Clean everything and start fresh
docker compose down -v
docker system prune -f
./test-docker-pipeline.sh
```

## 🏁 Stop the System

```bash
# Stop all services
docker compose down

# Stop and remove volumes (WARNING: deletes data)
docker compose down -v

# Clean built images
docker compose down --rmi local
```

## 📁 Project Structure

```
wikimedia-lab/
├── docker-compose.yml              # Main orchestration file
├── test-docker-pipeline.sh         # Automated test script
├── producer-wikimedia/             # Wikimedia stream producer
│   ├── Dockerfile
│   └── boot/src/main/resources/
│       ├── application.yml         # Local config
│       └── application-docker.yml  # Docker config
├── transform-wikimedia/            # Event transformation service
│   ├── Dockerfile
│   └── boot/src/main/resources/
│       ├── application.yml         # Local config
│       └── application-docker.yml  # Docker config
└── consumer-wikimedia/             # MongoDB consumer + REST API
    ├── Dockerfile
    └── boot/src/main/resources/
        ├── application.yml         # Local config
        └── application-docker.yml  # Docker config
```

## 🔧 Recent Fixes

### Fixed Issues
- ✅ **YAML duplicate key error** in producer configuration
- ✅ **Transform service health checks** by adding `spring-boot-starter-web`
- ✅ **Docker compose health check timing** with realistic timeouts
- ✅ **Service dependencies** ensuring proper startup sequence
- ✅ **Apple Silicon compatibility** with ARM64-compatible base images

### Configuration Improvements
- ✅ **Separate Docker configurations** for each service
- ✅ **Environment variable mapping** in docker-compose
- ✅ **Health check optimization** with longer start periods
- ✅ **Automated cleanup script** for complete rebuilds

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
