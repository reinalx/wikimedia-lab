e h# 🚀 Generación AUTOMÁTICA y ESCALABLE de APIs - Guía de Uso

## 📋 Resumen

Este proyecto está configurado para **generar automáticamente TODAS las APIs** que añadas en `apis/openapi/` **SIN NECESIDAD DE TOCAR EL POM.XML**. 

🎯 **Ventaja Clave**: Solo crea la carpeta con la especificación OpenAPI y Maven automáticamente detecta y genera la API.

## 📁 Estructura de APIs

```
apis/openapi/
├── products/           # ✅ API de productos (activa)
│   ├── metadata.yml
│   ├── openapi-rest.yml
│   └── v1/
├── users/              # ✅ API de usuarios (activa)  
│   ├── metadata.yml
│   ├── openapi-rest.yml
│   └── v1/
└── orders/             # 🔮 Futura API (será detectada automáticamente)
    ├── metadata.yml
    ├── openapi-rest.yml
    └── v1/
```

## ✅ APIs Configuradas Actualmente

### 🔸 Products API
- **Input**: `apis/openapi/products/openapi-rest.yml`
- **Output**: `api-rest/target/generated-sources/openapi-products/`
- **Package API**: `com.proactivedevs.template.apirest.products.api`
- **Package Models**: `com.proactivedevs.template.apirest.products.model`

### 🔸 Users API
- **Input**: `apis/openapi/users/openapi-rest.yml`
- **Output**: `api-rest/target/generated-sources/openapi-users/`
- **Package API**: `com.proactivedevs.template.apirest.users.api`
- **Package Models**: `com.proactivedevs.template.apirest.users.model`

## 🚀 Cómo Añadir una Nueva API (SUPER SIMPLE)

Para añadir una nueva API (por ejemplo, `orders`), **¡solo necesitas 2 pasos!**:

### 1️⃣ Crear la Estructura de la API

```
apis/openapi/orders/
├── metadata.yml
├── openapi-rest.yml    # 🎯 Este archivo es la clave - DEBE existir
├── README.md
└── v1/
    ├── components/
    │   ├── errors/
    │   │   └── components.yml
    │   └── orders/
    │       └── components.yml
    └── services/
        └── orders/
            └── orders.yml
```

### 2️⃣ Ejecutar Maven

```bash
mvn clean generate-sources
```

**¡YA ESTÁ! 🎉** La API se detecta automáticamente y se genera sin tocar el `pom.xml`.

## 🔮 APIs Preparadas para Detección Automática

El sistema ya está preparado para detectar automáticamente estas APIs comunes:

- ✅ **products** - Gestión de productos
- ✅ **users** - Gestión de usuarios
- 🔮 **orders** - Gestión de pedidos
- 🔮 **payments** - Gestión de pagos
- 🔮 **inventory** - Gestión de inventario
- 🔮 **notifications** - Sistema de notificaciones
- 🔮 **reports** - Generación de reportes
- 🔮 **analytics** - Analíticas y métricas
- 🔮 **auth** - Autenticación y autorización
- 🔮 **admin** - Panel de administración

### 3️⃣ Crear el Controlador

Crea el controlador que implemente la interfaz generada:

```java
@RestController
@RequestMapping("/api/v1")
public class OrdersController implements OrdersApi {
    
    // Los métodos se autocompletan desde la interfaz generada
    
}
```

## 🔧 Comandos Útiles

### Limpiar y Regenerar Todo
```bash
mvn clean generate-sources
```

### Solo Compilar
```bash
mvn compile
```

### Ver las Clases Generadas
```bash
find api-rest/target/generated-sources -name "*.java" -type f
```

## 📊 Ubicaciones de Archivos Generados

- **Products**: `api-rest/target/generated-sources/openapi-products/src/main/java/com/proactivedevs/template/apirest/products/`
- **Users**: `api-rest/target/generated-sources/openapi-users/src/main/java/com/proactivedevs/template/apirest/users/`
- **Future APIs**: `api-rest/target/generated-sources/openapi-{api-name}/src/main/java/com/proactivedevs/template/apirest/{api-name}/`

## 🎯 Patrón de Naming

### Directorios
- **Input**: `apis/openapi/{api-name}/openapi-rest.yml`
- **Output**: `openapi-{api-name}`

### Packages
- **API**: `com.proactivedevs.template.apirest.{api-name}.api`
- **Models**: `com.proactivedevs.template.apirest.{api-name}.model`

### Ejecuciones Maven
- **ID**: `generate-{api-name}-api`

## ⚠️ Notas Importantes

1. **Consistencia**: Mantén la misma estructura para todas las APIs
2. **Naming**: Usa nombres en plural para las APIs (products, users, orders)
3. **Packages**: Sigue el patrón de packages establecido
4. **Versionado**: Mantén la estructura v1/ para futuras versiones
5. **Validación**: Todas las APIs usan validación Jakarta Bean Validation

## 🚀 Estado Actual

✅ **Products API** - Completamente configurada y funcional  
✅ **Users API** - Completamente configurada y funcional  
🔄 **Future APIs** - Listas para ser añadidas siguiendo este patrón

---

**Última actualización**: 25 de octubre de 2025
