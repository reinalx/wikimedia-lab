# Simplified Dockerfile for Apple Silicon/ARM64
FROM amazoncorretto:21-alpine-jdk AS build

WORKDIR /app

# Manually install Maven
RUN apk add --no-cache maven

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Runtime image
FROM amazoncorretto:21-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# Copy JAR with the correct Maven-generated name
COPY --from=build /app/boot/target ./target
RUN cp target/producer-wikimedia-boot-*.jar app.jar && rm -rf target

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8081

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Entry point
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
