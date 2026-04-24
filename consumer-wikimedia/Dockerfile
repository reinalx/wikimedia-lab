# Dockerfile optimizado usando Maven Wrapper (mvnw)
# Asegura la misma versión de Maven en todos los builds
FROM amazoncorretto:21-alpine-jdk AS build

WORKDIR /app

# Copy Maven Wrapper files first (ensures version consistency)
COPY consumer-wikimedia/mvnw .
COPY consumer-wikimedia/.mvn .mvn

# Make Maven Wrapper executable
RUN chmod +x mvnw

# Build and install wikimedia-schemas into local Maven repo
# Use package (not install) to avoid the kafka-schema-registry plugin bound to install phase
COPY wikimedia-schemas /wikimedia-schemas
RUN ./mvnw -f /wikimedia-schemas/pom.xml package -DskipTests -B && \
    ./mvnw install:install-file \
        -Dfile=/wikimedia-schemas/target/wikimedia-schemas-1.0.0-SNAPSHOT.jar \
        -DgroupId=com.learn \
        -DartifactId=wikimedia-schemas \
        -Dversion=1.0.0-SNAPSHOT \
        -Dpackaging=jar \
        -B

# Copy all POMs with proper directory structure
COPY consumer-wikimedia/pom.xml .
COPY consumer-wikimedia/api-rest/pom.xml api-rest/
COPY consumer-wikimedia/application/pom.xml application/
COPY consumer-wikimedia/boot/pom.xml boot/
COPY consumer-wikimedia/domain/pom.xml domain/
COPY consumer-wikimedia/infrastructure infrastructure/
COPY consumer-wikimedia/jacoco-report-aggregate/pom.xml jacoco-report-aggregate/

# Copy OpenAPI specs (required for code generation)
COPY consumer-wikimedia/apis apis/

# Download dependencies (cached layer if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code (infrastructure already copied)
COPY consumer-wikimedia/api-rest/src api-rest/src
COPY consumer-wikimedia/application/src application/src
COPY consumer-wikimedia/boot/src boot/src
COPY consumer-wikimedia/domain/src domain/src
COPY consumer-wikimedia/src src

# Build the application using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Runtime image
FROM amazoncorretto:21-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# Copy JAR with the correct Maven-generated name
COPY --from=build /app/boot/target ./target
RUN cp target/consumer-wikimedia-boot-*.jar app.jar && rm -rf target

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8090

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Entry point
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
