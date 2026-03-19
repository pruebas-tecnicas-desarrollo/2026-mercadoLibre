# Product Requirements Document / Documento de Requerimientos de Producto
## Product API – MercadoLibre Technical Challenge

---

## 1. Overview / Descripción General

### English

This document describes the **functional requirements**, **scope**, and **design decisions** of the Product API developed as part of the MercadoLibre backend technical challenge.

The objective of this API is to provide a **simple, consistent, and deterministic interface** for managing product data using **file-based persistence**, without relying on an external database.

### Español

Este documento describe los **requerimientos funcionales**, el **alcance** y las **decisiones de diseño** de la API de Productos desarrollada como parte del challenge técnico de backend de MercadoLibre.

El objetivo de esta API es proveer una **interfaz simple, consistente y determinística** para la gestión de productos utilizando **persistencia basada en archivos**, sin depender de una base de datos externa.

---

## 2. Problem Statement / Planteamiento del Problema

### English

The system must allow clients to:

- Create products with structured information
- Retrieve one or multiple products
- Update existing products
- Remove products individually or entirely

All operations must be **stateless**, **RESTful**, and **idempotent where applicable**.

### Español

El sistema debe permitir a los clientes:

- Crear productos con información estructurada
- Obtener uno o múltiples productos
- Actualizar productos existentes
- Eliminar productos de forma individual o total

Todas las operaciones deben ser **sin estado**, **RESTful** e **idempotentes cuando corresponda**.

---

## 3. Scope / Alcance

### English

#### In scope
- CRUD operations for products
- JSON file-based persistence
- Thread-safe read/write operations
- Input validation and consistent error handling
- Automatic ID generation
- Structured logging with correlation IDs

#### Out of scope
- Authentication and authorization
- Database engines (SQL / NoSQL)
- Product comparison logic beyond retrieving multiple items
- Pagination, sorting, or filtering
- External service integrations

### Español 

#### Dentro del alcance
- Operaciones CRUD sobre productos
- Persistencia en archivo JSON
- Operaciones de lectura/escritura thread-safe
- Validación de entrada y manejo consistente de errores
- Generación automática de IDs
- Logging estructurado con correlation IDs

#### Fuera del alcance
- Autenticación y autorización
- Motores de base de datos (SQL / NoSQL)
- Lógica de comparación de productos más allá de obtener múltiples ítems
- Paginación, ordenamiento o filtros
- Integraciones con servicios externos

---

## 4. Product Definition / Definición del Producto

### English

A product is defined by:
- A unique numeric identifier (`id`)
- An `information` object containing the product attributes

The API intentionally avoids generic abstractions such as `Model` and instead uses a **domain-specific `Product` concept** to improve clarity and expressiveness.

### Español

Un producto se define por:
- Un identificador numérico único (`id`)
- Un objeto `information` que contiene los atributos del producto

La API evita intencionalmente abstracciones genéricas como `Model` y utiliza un concepto de dominio explícito (`Product`) para mejorar la claridad y la expresividad del diseño.

---

## 5. Functional Requirements / Requerimientos Funcionales

### English

#### FR-1 Create product
- The system must allow creating a new product
- The product ID is generated automatically
- The operation returns the created product identifier

#### FR-2 Get product by ID
- The system returns product details for a given ID
- If the product does not exist, a `404 Not Found` error is returned

#### FR-3 Get all products
- The system returns all stored products
- If no products exist, an empty list is returned

#### FR-4 Get multiple products by IDs
- The system allows retrieving multiple products in a single request
- Non-existing product IDs are silently ignored
- Duplicate IDs are processed only once

#### FR-5 Update product
- The system updates the information of an existing product
- If the product does not exist, a `404 Not Found` error is returned

#### FR-6 Delete product by ID
- The system deletes a specific product by its ID
- The system validates product existence before deletion
- If the product does not exist, a  `404 Not Found` error is returned

#### FR-7 Delete all products
- The system deletes all stored products
- The storage is reset to an empty state

### Español

#### RF-1 Crear producto
- El sistema permite crear un nuevo producto
- El ID del producto se genera automáticamente
- La operación retorna el identificador creado

#### RF-2 Obtener producto por ID
- El sistema retorna los detalles del producto para un ID dado
- Si el producto no existe, se retorna un error `404 Not Found`

#### RF-3 Obtener todos los productos
- El sistema retorna todos los productos almacenados
- Si no existen productos, se retorna una lista vacía

#### RF-4 Obtener múltiples productos por ID
- El sistema permite obtener múltiples productos en una sola request
- IDs inexistentes se ignoran
- IDs duplicados se procesan una sola vez

#### RF-5 Actualizar producto
- El sistema actualiza la información de un producto existente
- Si el producto no existe, se retorna un error `404 Not Found`

#### RF-6 Eliminar producto por ID
- El sistema elimina un producto específico por su ID
- El sistema valida la existencia del producto antes de eliminarlo
- Si el producto no existe, se retorna un error `404 Not Found`

#### RF-7 Eliminar todos los productos
- El sistema elimina todos los productos almacenados
- El storage queda en estado vacío

---

## 6. Non-Functional Requirements

### English

#### NFR-1 Persistence
- Data is stored in a JSON file
- Read and write operations must be atomic and thread-safe

#### NFR-2 Logging & Observability
- Each request is assigned a correlation ID
- Logs include timestamp, level, class name, and correlation ID

#### NFR-3 Error Handling
- Errors return meaningful HTTP status codes
- Error messages are explicit and consistent

### Español

#### RNF-1 Persistencia
- Los datos se almacenan en un archivo JSON
- Las operaciones de lectura y escritura son atómicas y thread-safe

#### RNF-2 Logging y observabilidad
- Cada request posee un correlation ID
- Los logs incluyen timestamp, nivel, nombre de clase y correlation ID

#### RNF-3 Manejo de errores
- Los errores retornan códigos HTTP significativos
- Los mensajes de error son claros y consistentes

---

## 7. Design Decisions

### English

- File-based storage was selected to comply with challenge constraints
- The provided MVC architecture was preserved
- Business rules are encapsulated in the service layer
- DTOs decouple API contracts from domain models
- No echo or health endpoints were exposed to reduce attack surface

### Español

- Se eligió persistencia basada en archivos para cumplir con las restricciones del challenge
- Se mantuvo la arquitectura MVC provista
- Las reglas de negocio se encapsulan en la capa de servicio
- Los DTOs desacoplan el contrato de la API del dominio
- No se expusieron endpoints de echo o health para reducir la superficie de ataque

---

## 8. Success Criteria

### English

The solution is considered successful if:
- All functional requirements are met
- All provided tests pass successfully
- The API behaves deterministically
- The design is clear, readable, and REST-aligned

### Español

La solución se considera exitosa si:
- Todos los requerimientos funcionales se cumplen
- Todos los tests provistos pasan correctamente
- La API se comporta de forma determinística
- El diseño es claro, legible y alineado a principios REST
