# 🔐 Spring Boot 4 - JWT Auth Microservice Showcase

Un microservicio de autenticación robusto y listo para producción construido con **Spring Boot 4**, **Spring Security 7**, **JSON Web Tokens (JWT)** y **PostgreSQL**.

Este proyecto sirve como showcase para demostrar las mejores prácticas en la implementación de seguridad sin estado (_stateless_), arquitectura limpia y documentación de APIs RESTful.

---

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 4.1**
- **Spring Security 7** (Autenticación sin estado con JWT)
- **Spring Data JPA** (Persistencia de datos)
- **PostgreSQL** (Base de datos relacional)
- **Lombok** (Reducción de código repetitivo)
- **Springdoc OpenAPI / Swagger UI** (Documentación interactiva de la API)
- **Docker / Docker Compose** (Containerización del entorno)

---

## 🚀 Características Principales

- **Registro de usuarios (`/signup`)**: Creación de cuentas con contraseñas encriptadas mediante BCrypt.
- **Autenticación de usuarios (`/signin`)**: Validación de credenciales y generación de Access Token JWT.
- **Refresh Tokens**: Emisión de tokens de actualización para mantener sesiones sin necesidad de reautenticación frecuente.
- **Seguridad por Capas**: Configuración de reglas `SecurityFilterChain` con manejo estricto de excepciones.
- **Filtro JWT Personalizado**: Interceptación de peticiones para validar firma y expiración de tokens.
- **Documentación OpenAPI**: Documentación interactiva para probar endpoints directamente desde el navegador.

---

## 📋 Requisitos Previos

Asegúrate de tener instalado en tu máquina:

- **JDK 21** o superior
- **Maven 3.9+** (o usar el wrapper `./mvnw` incluido)
- **Docker y Docker Compose** (opcional, para levantar PostgreSQL fácilmente)

---

## ⚙️ Configuración e Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/Nicobgn/showcase-java-auth.git
cd showcase-java-auth
```

#### 1.1 Corriendo tests con Maven

```bash
./mvnw clean test
```

#### 1.2 Corriendo tests con Maven y detalles

```bash
./mvnw clean test -e
```

### 2. Iniciar el proyecto

```bash
./mvnw spring-boot:run
```
