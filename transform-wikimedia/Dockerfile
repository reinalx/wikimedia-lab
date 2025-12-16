# Dockerfile optimizado usando Maven Wrapper (mvnw)
# Asegura la misma versión de Maven en todos los builds
FROM amazoncorretto:21-alpine-jdk AS build

WORKDIR /app

# Copy Maven Wrapper files first (ensures version consistency)
COPY mvnw .
COPY .mvn .mvn

# Make Maven Wrapper executable
RUN chmod +x mvnw

# Copy all POMs with proper directory structure
COPY pom.xml .
COPY application/pom.xml application/
COPY boot/pom.xml boot/
COPY domain/pom.xml domain/
COPY infrastructure infrastructure/
COPY jacoco-report-aggregate/pom.xml jacoco-report-aggregate/

# Download dependencies (cached layer if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code (infrastructure already copied)
COPY application/src application/src
COPY boot/src boot/src
COPY domain/src domain/src
COPY src src

# Build the application using Maven Wrapper
RUN ./mvnw clean package -DskipTests

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
RUN cp target/transform-wikimedia-boot-*.jar app.jar && rm -rf target

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8092

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Entry point
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
