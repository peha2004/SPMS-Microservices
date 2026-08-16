# 🅿️ Smart Parking Management System (SPMS)

The **Smart Parking Management System (SPMS)** is a cloud-native, microservices-based platform designed for **real-time urban parking management**.

The system allows drivers to register vehicles, search and reserve parking spaces, record vehicle entry and exit, and make payments. Parking owners can create, update, and manage their own parking spaces while monitoring parking availability.

---

## 🏗️ Architecture Overview

The system is built using a **Distributed Microservices Architecture** with the following components:

### 🔹 Infrastructure Services

- **Service Registry (Netflix Eureka)**
    - Provides dynamic service registration and discovery.
    - Allows microservices to locate and communicate with each other.

- **Config Server**
    - Provides centralized configuration management.
    - Uses a local native configuration repository containing configuration files for each microservice.

- **API Gateway**
    - Acts as the single entry point for all client requests.
    - Handles request routing between microservices.
    - Performs JWT validation and role-based access control.

---

### 🔹 Domain Microservices

- **User Service**
    - Manages user registration and authentication.
    - Handles DRIVER and OWNER roles.
    - Generates secure JWT access tokens.
    - Provides user profile management.

- **Parking Service**
    - Manages parking spaces.
    - Allows parking owners to create, update, and delete their own parking spaces.
    - Provides parking availability tracking.
    - Supports parking reservation and release.
    - Provides location-based parking search.

- **Vehicle Service**
    - Manages driver vehicle registration and ownership.
    - Allows drivers to update and delete their own vehicles.
    - Simulates vehicle entry and exit tracking.

- **Payment Service**
    - Processes mock card payments.
    - Simulates successful and failed payment transactions.
    - Generates digital payment receipts.
    - Allows drivers to view their own payment history.
    - Allows owners to view payment information across the system.

---

## 🚀 Startup Instructions (Order of Operations)

To ensure the system starts correctly, the services should be launched in the following order.

### 1️⃣ Infrastructure Layer

#### Service Registry

- Start the `service-registry` module.
- Port: `8761`
- Eureka Dashboard:

  [http://localhost:8761](http://localhost:8761)

The Eureka server must be running before starting the other services so that they can register themselves.

#### Config Server

- Start the `config-server` module.
- Port: `8888`
- Configuration files are stored in:

  `config-server/src/main/resources/configs`

The Config Server provides centralized configuration to the microservices.

#### API Gateway

- Start the `api-gateway` module.
- Port: `8080`

The API Gateway acts as the main entry point for the application.

---

### 2️⃣ Domain Layer

Once the infrastructure services are running, start the domain microservices.

- **User Service**
    - Port: `8081`
    - Database: `spms_user_db`

- **Parking Service**
    - Port: `8082`
    - Database: `spms_parking_db`

- **Vehicle Service**
    - Port: `8083`
    - Database: `spms_vehicle_db`

- **Payment Service**
    - Port: `8084`
    - Database: `spms_payment_db`

The domain services can be started in any order after the Service Registry, Config Server, and API Gateway are available.

---

## 🔄 Request Flow

All client requests are sent through the **API Gateway**.

```text
                    ┌─────────────────────┐
                    │       Client        │
                    │  Postman / Frontend │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     API Gateway     │
                    │      Port 8080      │
                    │ JWT + Authorization │
                    └──────────┬──────────┘
                               │
             ┌─────────────────┼─────────────────┐
             │                 │                 │
             ▼                 ▼                 ▼
      ┌─────────────┐   ┌─────────────┐   ┌─────────────┐
      │    User     │   │   Parking   │   │   Vehicle   │
      │   Service   │   │   Service   │   │   Service   │
      │    :8081    │   │    :8082    │   │    :8083    │
      └─────────────┘   └─────────────┘   └─────────────┘
                               │
                               ▼
                        ┌─────────────┐
                        │   Payment   │
                        │   Service   │
                        │    :8084    │
                        └─────────────┘

                  ┌─────────────────────┐
                  │   Eureka Server     │
                  │      :8761          │
                  └─────────────────────┘

                  ┌─────────────────────┐
                  │   Config Server     │
                  │      :8888          │
                  └─────────────────────┘
```

---

## 🔐 Security Configuration

**Global Security**
Implemented at the API Gateway level via a global JWT filter that runs on every incoming request.

**JWT Validation**
All requests require a valid `Authorization: Bearer <token>` header, except the public endpoints listed below.

**Public Endpoints**
- `POST /api/users/register`
- `POST /api/users/login`

**Role-Based Access Control**

| Action | DRIVER | OWNER |
|---|---|---|
| Create / update / delete a parking space | ❌ | ✅ (own spaces only) |
| Register / update / delete a vehicle | ✅ (own vehicles only) | ❌ |
| Make a payment | ✅ | ❌ |
| View own profile / payment history | ✅ | ✅ |
| View all payments / receipts | ❌ | ✅ |
| View / search parking spaces | ✅ | ✅ |
| Reserve / release a parking slot | ✅ | ✅ |
| Record vehicle entry / exit | ✅ | ✅ |

---

## 🛠️ Technology Stack

| Category | Technology |
|---|---|
| Framework | Spring Boot 4.1.0 |
| Cloud Tools | Spring Cloud (Gateway, Eureka, Config) |
| Database | MySQL (one database per service) |
| Communication | Synchronous HTTP / JSON |
| Security | JWT & BCrypt Encryption |
| Utilities | Lombok, Jackson |

---

## 📂 Project Structure & Submission Files

- `/config-server/src/main/resources/configs` — Centralized YAML/properties configuration files, one per service
- `/docs/screenshots` — Contains the Eureka Dashboard screenshot showing all services UP
- `postman_collection.json` — Import into Postman to test the full system flow

---

## 🧪 Testing the System (Postman)

1. **Register / Login** — Obtain an `accessToken` from the User Service. Register both a DRIVER and an OWNER to test role-based access.
2. **Create Parking** — As OWNER: `POST /api/parking`
3. **Register Vehicle** — As DRIVER: `POST /api/vehicles`
4. **Simulate Entry / Exit** — `POST /api/vehicles/{id}/entry` and `POST /api/vehicles/{id}/exit`
5. **Process Payment** — As DRIVER: `POST /api/payments`
6. **Verify Role Restrictions** — Confirm a DRIVER gets `403` when creating a parking space, and an OWNER gets `403` when registering a vehicle or making a payment.

---

## Resources

- [Postman Collection](./postman_collection.json)
- ![Eureka Dashboard](./docs/screenshots/eureka_dashboard.png)

---

## 👨‍💻 Developed By

**Name:** Ranuthi Pehansa Peiris