# Maven Wrapper Migration - Documentation

## 🎯 Objetivo
Migrar los Dockerfiles para usar Maven Wrapper (mvnw) en lugar de instalar Maven manualmente, garantizando versiones consistentes y builds reproducibles.

## ✅ Cambios Realizados

### Servicios Actualizados
- ✅ **consumer-wikimedia** - Dockerfile actualizado
- ✅ **producer-wikimedia** - Dockerfile actualizado  
- ✅ **transform-wikimedia** - Dockerfile actualizado

### Permisos de Ejecución
Todos los archivos `mvnw` ahora tienen permisos de ejecución:
```bash
chmod +x consumer-wikimedia/mvnw
chmod +x producer-wikimedia/mvnw
chmod +x transform-wikimedia/mvnw
```

## 🔄 Estructura del Dockerfile (Antes vs Después)

### ❌ ANTES (Instalando Maven manualmente)
```dockerfile
FROM amazoncorretto:21-alpine-jdk AS build
WORKDIR /app

# Instalaba Maven desde repos Alpine
RUN apk add --no-cache maven

COPY pom.xml .
COPY */pom.xml */

# Descargaba dependencias
RUN mvn dependency:go-offline -B

# Copiaba código y compilaba
COPY application application
COPY boot boot
...
RUN mvn clean package -DskipTests
```

**Problemas:**
- ❌ Versión de Maven variable según Alpine
- ❌ Imagen más pesada
- ❌ Builds no reproducibles
- ❌ Dependencia de repositorios externos

### ✅ DESPUÉS (Usando Maven Wrapper)
```dockerfile
FROM amazoncorretto:21-alpine-jdk AS build
WORKDIR /app

# Copia el Maven Wrapper (versión consistente)
COPY mvnw .
COPY .mvn .mvn

# Da permisos de ejecución
RUN chmod +x mvnw

# Copia configuración Maven
COPY pom.xml .
COPY */pom.xml */

# Descarga dependencias (capa cacheada)
RUN ./mvnw dependency:go-offline -B

# Copia código fuente
COPY application application
COPY boot boot
COPY domain domain
COPY infrastructure infrastructure
COPY jacoco-report-aggregate jacoco-report-aggregate
COPY src src

# Compila usando el wrapper
RUN ./mvnw clean package -DskipTests
```

**Beneficios:**
- ✅ Versión de Maven fija (definida en `.mvn/wrapper/maven-wrapper.properties`)
- ✅ Imagen más ligera (no instala Maven)
- ✅ Builds 100% reproducibles
- ✅ Sin dependencias de repos externos
- ✅ Mejor caching de Docker (dependencias en capa separada)

## 📊 Ventajas de Maven Wrapper

### 1. Consistencia de Versión
El archivo `.mvn/wrapper/maven-wrapper.properties` define la versión exacta:
```properties
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.5/apache-maven-3.9.5-bin.zip
```

### 2. Portabilidad
Cualquier desarrollador o CI/CD puede ejecutar el build sin instalar Maven:
```bash
./mvnw clean package  # Funciona en cualquier máquina
```

### 3. Builds Reproducibles
- Mismo resultado en desarrollo, CI/CD y producción
- Mismas versiones de plugins
- Misma resolución de dependencias

### 4. Optimización de Docker Cache
```dockerfile
# Capa 1: Wrapper files (cambia raramente)
COPY mvnw .
COPY .mvn .mvn

# Capa 2: POMs (cambia ocasionalmente)
COPY pom.xml .
COPY */pom.xml */

# Capa 3: Dependencias (se cachea si POMs no cambian)
RUN ./mvnw dependency:go-offline -B

# Capa 4: Código fuente (cambia frecuentemente)
COPY application application
...

# Capa 5: Build (solo si código o dependencias cambian)
RUN ./mvnw clean package -DskipTests
```

## 🚀 Comandos para Testing

### Build individual de servicios
```bash
# Consumer
cd consumer-wikimedia
docker build -t consumer-wikimedia:test .

# Producer
cd producer-wikimedia
docker build -t producer-wikimedia:test .

# Transform
cd transform-wikimedia
docker build -t transform-wikimedia:test .
```

### Build completo con docker-compose
```bash
docker-compose build
```

### Verificar que usa mvnw
```bash
# Buscar en el build log
docker build consumer-wikimedia 2>&1 | grep mvnw
# Debería mostrar: "./mvnw dependency:go-offline"
# Y: "./mvnw clean package"
```

## 📝 Notas Importantes

### Permisos en Git
Los archivos `mvnw` están en Git con permisos de ejecución:
```bash
git ls-files --stage | grep mvnw
# 100755 indica permisos de ejecución
```

### Estructura .mvn/
Cada proyecto mantiene su configuración del wrapper:
```
consumer-wikimedia/.mvn/
├── wrapper/
│   ├── maven-wrapper.jar
│   └── maven-wrapper.properties
```

### Variables de Entorno (opcional)
Se pueden configurar opciones de Maven:
```dockerfile
ENV MAVEN_OPTS="-Xmx512m"
```

## 🔍 Troubleshooting

### Error: "mvnw: Permission denied"
```bash
# Solución: Dar permisos
chmod +x */mvnw
git add --chmod=+x */mvnw
git commit -m "Add execute permission to mvnw"
```

### Error: "mvnw not found"
```dockerfile
# Asegurarse de copiar el wrapper antes de usarlo
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw
```

### Limpiar cache de Docker
```bash
# Si hay problemas con cache antiguo
docker builder prune -a
docker-compose build --no-cache
```

## ✨ Resultado Final

Los tres servicios ahora:
1. ✅ Usan Maven Wrapper consistentemente
2. ✅ No requieren instalación de Maven
3. ✅ Tienen builds reproducibles
4. ✅ Optimizan el cache de Docker
5. ✅ Siguen las mejores prácticas de Docker + Maven

---

**Fecha de migración:** 16/12/2025  
**Autor:** Cline AI Assistant  
**Versión de Maven Wrapper:** Definida en cada `.mvn/wrapper/maven-wrapper.properties`
