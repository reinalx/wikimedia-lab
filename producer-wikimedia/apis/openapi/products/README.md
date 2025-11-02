# 🧩 Products API

## Overview

This is an **example API definition** used to demonstrate how to organize and define APIs within this project’s structure.  
Each API (e.g., `products`, `purchases`, `users`) must follow **the same directory layout** so that the automatic OpenAPI code generation works correctly.

---

## 📁 Directory Structure

All APIs must be placed under the `/apis/openapi` folder in the repository root.  
Each API lives inside its own subfolder, which must contain **three required files**:

``` 
└── /openapi
├── /products
│ ├── metadata.yml
│ ├── openapi-rest.yml
│ └── README.md
├── /purchases
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

## 🧾 Example Documentation Template

Below is a **template** for what each API README should include.  
When you create a new API (e.g., `purchases`, `users`), update these sections with your specific information.

---

## Summary

Welcome to the documentation for the **Products API**.  
This API allows you to manage and retrieve product information within the system.

---

## About

The **Products API** provides access to product-related operations, including creation, update, and retrieval of product details.  
It serves as an example for structuring and documenting APIs in this monorepo.

---

## Configuration

Before using the API, you must configure authentication credentials.  
The API uses **Basic Authentication** or **Bearer Tokens** depending on your backend configuration.  
Include your credentials in every request header to access protected endpoints.

---

## Documentation

This API is defined using the **OpenAPI 3.0.3** specification.  
The main specification file is [`openapi-rest.yml`](./openapi-rest.yml).

It includes the following endpoints:

1. `GET /products` — Retrieve a list of products.
2. `POST /products` — Create a new product.
3. `GET /products/{id}` — Retrieve details for a specific product.

Refer to `openapi-rest.yml` for detailed endpoint specifications.

---

## Example

### Returns list of products

```http
GET /products
Authorization: Basic your_base64_encoded_credentials
Accept: application/json
