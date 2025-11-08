# 🧩 Wikimedia Events API

## Overview

This API provides comprehensive event management functionality for the Wikimedia system.  
It allows managing and retrieving Wikimedia event information, enabling real-time monitoring and analysis of Wikimedia activities.

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

Welcome to the documentation for the **Wikimedia Events API**.  
This API allows you to manage and retrieve Wikimedia event information, providing real-time access to activities and changes within the Wikimedia ecosystem.

---

## About

The **Wikimedia Events API** provides comprehensive event management capabilities including:
- Real-time event streaming and monitoring
- Event data retrieval and filtering
- Historical event analysis
- Integration with Wikimedia data sources
- Event-driven architecture support

This API serves as a central hub for consuming and processing Wikimedia events, enabling applications to react to changes and updates across the Wikimedia platform.

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

1. `GET /v1/events` — Retrieve Wikimedia events with optional filtering and pagination
2. `POST /v1/events` — Create or register new events (if applicable)
3. `GET /v1/events/{id}` — Retrieve specific event details by ID

Refer to `openapi-rest.yml` for detailed endpoint specifications, request/response schemas, and authentication requirements.

---

## Example

### Retrieve Events

```http
GET /v1/events
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/json
```

### Retrieve Events with Filters

```http
GET /v1/events?type=edit&limit=50&offset=0
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/json
```

### Response Example

```json
{
  "events": [
    {
      "id": "event-123",
      "type": "page-edit",
      "timestamp": "2025-11-08T18:30:00Z",
      "user": "WikiUser123",
      "title": "Sample Article",
      "summary": "Updated content section"
    }
  ],
  "pagination": {
    "total": 1250,
    "limit": 50,
    "offset": 0,
    "hasMore": true
  }
}
```

---

## Maintainer

**Andrés Reinaldo Cid**  
Email: andres@example.com

---

## Version

Current API Version: **0.0.1**

---

## Base Path

API Base Path: `/wikimedia`  
Full URL: `http://localhost:8080/wikimedia`
