# Product API – MercadoLibre Technical Challenge

## 1. API Design / Diseño de api

### English

The API design follows the **base project structure provided in the challenge**, which is a classic **MVC architecture implemented with Java and Spring Boot**.  
However, several structural and conceptual changes were introduced in order to better align the solution with the problem domain and RESTful best practices.

<details>
  <summary><strong>Applied changes</strong></summary>

  **A)** All references to the generic `Model` concept were removed.  
  Since the requirement explicitly focuses on **products**, introducing a concrete and meaningful domain noun (`Product`) was considered a clearer and more expressive design choice.

  **B)** The original endpoint concepts were preserved, with the following exceptions:
  - `GET /`  
    This endpoint was internally repurposed as `getAllProducts`. Although it could have been exposed as an echo or health endpoint, it was intentionally not added due to security and scope considerations.
  - `DELETE /products/erased`  
    This endpoint was removed in favor of a more RESTful approach using `DELETE /products`.

  **C)** Existing test cases were updated to reflect the new API behavior and structural changes.
</details>

### Español

El diseño de la API sigue la **estructura base del proyecto provisto en el challenge**, el cual corresponde a una arquitectura **MVC clásica desarrollada con Java y Spring Boot**.  
No obstante, se realizaron múltiples cambios estructurales y conceptuales con el objetivo de alinear mejor la solución al dominio del problema y a las buenas prácticas RESTful.

<details>
  <summary><strong>Cambios aplicados</strong></summary>

  **A)** Se eliminaron todas las referencias al concepto genérico `Model`.  
  Dado que el requerimiento se centra explícitamente en **productos**, se consideró más claro y expresivo introducir un sustantivo de dominio concreto (`Product`) y construir la solución a partir de él.

  **B)** Se mantuvieron los conceptos originales de los endpoints, con las siguientes excepciones:
  - `GET /`  
    Este endpoint fue reutilizado internamente para `getAllProducts`. Si bien podría haberse expuesto como un endpoint de echo o health, se decidió no hacerlo por razones de seguridad y alcance.
  - `DELETE /products/erased`  
    Este endpoint fue eliminado en favor de un diseño más RESTful utilizando `DELETE /products`.

  **C)** Los casos de prueba existentes fueron modificados para adaptarse al nuevo comportamiento de la API y a los cambios estructurales realizados.
</details>

---

## 2. Key Architectural Decisions / Decisiones técnicas de arquitectura

### English

- **Docker** was implemented to ensure a consistent and reproducible execution environment.
- Models classes were intentionally **not annotated with `@Entity`**, since no relational database is used. 
- A **Correlation ID filter** was added to every request in order to improve observability and request traceability.
- All documentation, comments, and code were written in **English**, following the language used in the original requirements and base project.

### Español

- Se implementó **Docker** para garantizar un entorno de ejecución consistente y reproducible.
- Las clases de los modelos **no fueron anotadas con `@Entity`**, ya que no se utiliza una base de datos relacional.  
  Considerando el alcance del challenge y el uso de un archivo JSON como mecanismo de persistencia, se decidió mantenerlas como modelos de dominio simples.
- Se agregó un **filtro de Correlation ID** a todas las peticiones con el objetivo de mejorar la observabilidad y la trazabilidad de los requests.
- Toda la documentación, comentarios y el desarrollo del código fueron realizados en **inglés**, siguiendo el lenguaje utilizado en los requerimientos y en el proyecto base.

---
## 3. Technologies Used / Tecnologías utilizadas

### English

- **Java 17** – Primary programming language
- **Spring Boot** – Application framework and REST API development
- **Spring Web (MVC)** – Controller layer and HTTP request handling
- **Maven** – Dependency management and build tool
- **Docker & Docker Compose** – Containerization and environment consistency
- **Jackson** – JSON serialization and deserialization
- **Lombok** – Boilerplate code reduction
- **JUnit / Mockito** – Unit and integration testing
- **SLF4J + Logback** – Logging abstraction and implementation
- **Springdoc OpenAPI** – API documentation
- **ChatGPT (OpenAI)** – Assisted with documentation refinement, code review, and architectural reasoning

### Español

- **Java 17** – Lenguaje principal de desarrollo
- **Spring Boot** – Framework de la aplicación y desarrollo de la API REST
- **Spring Web (MVC)** – Capa de controladores y manejo de requests HTTP
- **Maven** – Gestión de dependencias y build
- **Docker & Docker Compose** – Contenerización y consistencia del entorno
- **Jackson** – Serialización y deserialización JSON
- **Lombok** – Reducción de código boilerplate
- **JUnit / Mockito** – Testing unitario e integración
- **SLF4J + Logback** – Abstracción e implementación de logging
- **Springdoc OpenAPI** – Documentación de la API
- **ChatGPT (OpenAI)** – Asistencia en la redacción de documentación, revisión de código y razonamiento arquitectónico

---
## 4. Main Endpoints / Endpoints principales

```https
GET    /products
GET    /products/{id}
GET    /products?ids=n,n1,n2...n9
POST   /products
PUT    /products/{id}
DELETE /products
DELETE /products/{id}
```

---
## 5. Setup Instructions / Instrucciones de uso

### Docker

Option 1 / Opción 1:
```bash
docker compose up -d
```

Option 2 / Opción 2:
```powershell
./run.ps1
```


### Local

```bash
mvn clean install
mvn spring-boot:run
```
