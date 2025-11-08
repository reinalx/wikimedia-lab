# 📊 Wikimedia Analysis API

## Overview

This API provides event analysis functionality for the Wikimedia system.  
It handles the creation and management of analysis records for Wikimedia events, including content analysis and sentiment classification.

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

Welcome to the documentation for the **Wikimedia Analysis API**.  
This API enables the creation and management of analysis records for Wikimedia events, providing insights and sentiment analysis capabilities.

---

## About

The **Wikimedia Analysis API** provides comprehensive event analysis capabilities including:
- Event analysis creation and storage
- Content analysis for Wikimedia events
- Sentiment classification (positive, neutral, negative)
- Integration with Wikimedia event processing system
- Secure access control with JWT authentication

---

## Configuration

Before using the API, you must configure JWT authentication credentials.  
The API uses Bearer token authentication for secure access to protected endpoints.  
Include your JWT token in the Authorization header for every request.

---

## Documentation

This API is defined using the **OpenAPI 3.0.3** specification.  
The main specification file is [`openapi-rest.yml`](./openapi-rest.yml).

It includes the following endpoints:

1. `POST /v1/analysis` — Create a new analysis for a Wikimedia event

Refer to `openapi-rest.yml` for detailed endpoint specifications, request/response schemas, and authentication requirements.

---

## Example

### Create Event Analysis

```http
POST /v1/analysis
Content-Type: application/json
Accept: application/json
Authorization: Bearer <your-jwt-token>

{
  "eventId": "339339603",
  "analysis": "This event shows a significant increase in user engagement with positive community response.",
  "sentiment": "positive"
}
```

### Response

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "message": "Analysis created successfully"
}
```

---

## Schema Details

### CreateEventAnalysisRequest

| Field | Type | Required | Description | Example |
|-------|------|----------|-------------|---------|
| `eventId` | string | ✅ | Unique identifier for a Wikimedia event (1-100 chars) | `"339339603"` |
| `analysis` | string | ✅ | Analysis content for the event (1-5000 chars) | `"This event shows..."` |
| `sentiment` | string | ❌ | Sentiment classification | `"positive"`, `"neutral"`, `"negative"` |

---

## Authentication

This API uses **JWT Bearer Token** authentication. Include the token in your requests:

```http
Authorization: Bearer <your-jwt-token>
```

---

## Error Responses

The API returns standard HTTP status codes:

- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Missing or invalid authentication token
- `403 Forbidden` - Access denied for the requested resource
- `404 Not Found` - Event or resource not found
- `500 Internal Server Error` - Server-side error

---

## Maintainer

**Andrés Reinaldo Cid**  
Email: andresrc345@gmail.com

---

## Version

Current API Version: **0.0.1**

---

## Base Path

API Base Path: `/wikimedia`  
Full URL: `http://localhost:8080/wikimedia`
