# 🔐 Auth Service

The **Auth Service** is the central security authority of the microservice ecosystem. It handles user authentication, credential verification via the `user-service`, and issues signed **JSON Web Tokens (JWT)** for stateless authorization.

---

## 🛠 Features

* **Login & Authentication:** Verifies user credentials against the User Service.
* **JWT Issuance:** Generates secure, signed tokens containing user identity and roles.
* **Password Security:** Utilizes `BCrypt` for secure password hashing and matching.
* **Token Validation:** Provides logic to parse and verify incoming JWTs for downstream services.

---

## 🔄 Authentication Flow

The Auth Service acts as a gateway between the client and the internal User Service.



1.  **Client** sends `username` and `password` to `/api/v1/auth/login`.
2.  **Auth Service** requests user details from **User Service**.
3.  **Auth Service** compares the passwords.
4.  If valid, **Auth Service** signs a JWT and returns it to the **Client**.

---

## 🚀 Getting Started

### Prerequisites
* Java 17+
* Access to the running `user-service`
* PostgreSQL (for token blacklisting or audit logs, if applicable)

### Configuration
Add the following to your `application.properties`:

```properties
# JWT Configuration
app.jwt.secret=your_very_long_and_very_secure_secret_key_here
app.jwt.expiration-ms=3600000

# Service Communication
services.user-service.url=http://localhost:8080
