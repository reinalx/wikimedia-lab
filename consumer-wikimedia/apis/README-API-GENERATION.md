# 🚀 AUTOMATIC and SCALABLE API Generation - Usage Guide

## 📋 Overview

This project is configured to **automatically generate ALL APIs** that you add under `apis/openapi/` — **without ever touching the `pom.xml`**.

🎯 **Key Advantage:** Just create the folder with your OpenAPI specification, and Maven will automatically detect and generate the API for you.

---

## 📁 API Structure



```
apis/openapi/
├── products/           # ✅ Products API (active)
│   ├── metadata.yml
│   ├── openapi-rest.yml
│   └── v1/
├── users/              # ✅ Users API (active)  
│   ├── metadata.yml
│   ├── openapi-rest.yml
│   └── v1/
└── orders/             # 🔮 Future API (auto-detectable)
    ├── metadata.yml
    ├── openapi-rest.yml
    └── v1/
```


---

## ✅ Currently Configured APIs

### 🔸 Products API
- **Input:** `apis/openapi/products/openapi-rest.yml`
- **Output:** `api-rest/target/generated-sources/openapi-products/`
- **API Package:** `com.proactivedevs.template.apirest.products.api`
- **Models Package:** `com.proactivedevs.template.apirest.products.model`

### 🔸 Users API
- **Input:** `apis/openapi/users/openapi-rest.yml`
- **Output:** `api-rest/target/generated-sources/openapi-users/`
- **API Package:** `com.proactivedevs.template.apirest.users.api`
- **Models Package:** `com.proactivedevs.template.apirest.users.model`

---

## 🚀 How to Add a New API (SUPER EASY)

To add a new API (for example, `orders`), you only need **2 steps**!

### 1️⃣ Create the API Folder Structure



```
apis/openapi/orders/
├── metadata.yml
├── openapi-rest.yml    # 🎯 This is the key file – it MUST exist
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

### 2️⃣ Run Maven

```bash
mvn clean generate-sources
```


### 3️⃣ Create the Controller

Then, create a controller that implements the generated interface:

```java
@RestController
@RequestMapping("/api/v1")
public class OrdersController implements OrdersApi {

    // Methods will auto-complete from the generated interface
    
}
```

## 🔧 Useful Commands

### Clean and Regenerate Everything
```bash
mvn clean generate-sources
```

### Compile Only
```bash
mvn compile
```

### Compile Only
```bash
find api-rest/target/generated-sources -name "*.java" -type f
```

## 📊 Generated Files Location

- **Products**: `api-rest/target/generated-sources/openapi-products/src/main/java/com/proactivedevs/template/apirest/products/`
- **Users**: `api-rest/target/generated-sources/openapi-users/src/main/java/com/proactivedevs/template/apirest/users/`
- **Future APIs**: `api-rest/target/generated-sources/openapi-{api-name}/src/main/java/com/proactivedevs/template/apirest/{api-name}/`

## 🎯 Naming Conventions

### Directories
- **Input**: `apis/openapi/{api-name}/openapi-rest.yml`
- **Output**: `openapi-{api-name}`

### Packages
- **API**: `com.proactivedevs.template.apirest.{api-name}.api`
- **Models**: `com.proactivedevs.template.apirest.{api-name}.model`

### Ejecuciones Maven
- **ID**: `generate-{api-name}-api`

## ⚠️ Notas Importantes

- **Consistency:** Maintain the same structure for all APIs.

- **Naming:** Always use plural names (e.g., `products`, `users`, `orders`).

- **Packages:** Follow the defined package pattern.

- **Versioning:** Keep the `v1/` structure for future versions.

- **Validation:** All APIs support Jakarta Bean Validation.

## 🚀 Current Status

✅ **Products API** – Fully configured and functional  
✅ **Users API** – Fully configured and functional  
🔄 **Future APIs** – Ready to be added following this pattern
---

**Última actualización**: 25 de octubre de 2025
