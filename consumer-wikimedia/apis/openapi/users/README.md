# 🧩 Wikimedia Users API

## Overview

This API provides user management and authentication functionality for the Wikimedia system.  
It handles user authentication, login operations, and user-related analysis within the project's structure.

---

## 📁 Directory Structure

All APIs must be placed under the `/apis/openapi` folder in the repository root.  
Each API lives inside its own subfolder, which must contain **three required files**:

``` 
└── /openapi
├── /analysis
│ ├── metadata.yml
│ ├── openapi-rest.yml
│ └── README.md
├── /events
│ ├── metadata.yml
│ ├── openapi-rest.yml
│ └── README.md
└── /users
├── metadata.yml
├── openapi-rest.yml
└── README.md
```

### 📌 Mandatory Files

| File | Description |
|------|--------------|
| `metadata.yml` | Contains metadata about the API (name, version, maintainers, description, etc.). |
| `openapi-rest.yml` | The **OpenAPI 3.x specification** file that defines endpoints, models, and schemas. |
| `README.md` | Documentation file for describing the purpose and usage of the API (this file). |

> ⚠️ **Important:**  
> This structure **must be strictly respected**.  
> The code generation process looks for `/apis/openapi/<api-name>/openapi-rest.yml`.  
> If that file is missing or located elsewhere, the build will fail and no code will be generated for that API.

---

## 🧠 About OpenAPI

[OpenAPI](https://www.openapis.org/) (formerly known as Swagger) is a specification for describing RESTful APIs.  
It allows developers to define endpoints, input/output models, authentication mechanisms, and responses in a single YAML or JSON file.

By following the OpenAPI 3.0.3 format, tools like **OpenAPI Generator** can automatically produce:
- Java interfaces (controllers)
- DTOs and models
- Client SDKs
- Interactive documentation (Swagger UI)

This project uses **OpenAPI Generator** to create the API interfaces automatically during the Maven build phase.

---

## Summary

Welcome to the documentation for the **Wikimedia Users API**.  
This API allows you to manage user authentication, login operations, and perform user-related analysis within the Wikimedia system.

---

## About

The **Wikimedia Users API** provides comprehensive user management capabilities including:
- User authentication and authorization
- Login and session management
- User data analysis and insights
- Secure access control for Wikimedia events and operations

---

## Configuration

Before using the API, you must configure authentication credentials.  
The API uses **Bearer Token (JWT)** authentication.  
Include your JWT token in every request header to access protected endpoints.

```http
Authorization: Bearer your_jwt_token_here
```

---

## Documentation

This API is defined using the **OpenAPI 3.0.3** specification.  
The main specification file is [`openapi-rest.yml`](./openapi-rest.yml).

It includes the following endpoints:

1. `GET /v1/events` — Retrieve Wikimedia events (user context)
2. `POST /v1/auth/login` — Authenticate user and obtain JWT token
3. `GET /v1/analysis` — Retrieve user analysis data

Refer to `openapi-rest.yml` for detailed endpoint specifications, request/response schemas, and authentication requirements.

---

## Example

### User Login

```http
POST /v1/auth/login
Content-Type: application/json
Accept: application/json

{
  "username": "your_username",
  "password": "your_password"
}
```

### Access Protected Resources

```http
GET /v1/events
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/json
```

---

## Maintainer

**Andrés Reinaldo Cid**  
Email: andres@example.com

---

## Version

Current API Version: **0.0.1**
