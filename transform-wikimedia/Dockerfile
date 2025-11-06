# Dockerfile simplificado para Apple Silicon/ARM64
FROM amazoncorretto:21-alpine-jdk AS build

WORKDIR /app

# Instalar Maven manualmente
RUN apk add --no-cache maven

# Copiar código fuente
COPY . .

# Compilar aplicación
RUN mvn clean package -DskipTests

# Imagen de runtime
FROM amazoncorretto:21-alpine

WORKDIR /app

# Instalar curl para health checks
RUN apk add --no-cache curl

# Crear usuario no-root
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# Copiar JAR con el nombre correcto generado por Maven
COPY --from=build /app/boot/target ./target
RUN cp target/transform-wikimedia-boot-*.jar app.jar && rm -rf target

# Cambiar propietario
RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
