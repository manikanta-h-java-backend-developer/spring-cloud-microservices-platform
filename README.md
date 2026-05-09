# 🚀 Spring Cloud Microservices Platform

> Production-ready microservices architecture using Spring Boot & Spring Cloud with API Gateway, Service Discovery, Config Server, Kafka messaging, Resilience4J fault tolerance, distributed tracing via Zipkin, and full observability with Prometheus & Grafana. Deployed on Kubernetes with CI/CD.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Services](#services)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
    - [1. Clone the Repository](#1-clone-the-repository)
    - [2. Configure the Config Server](#2-configure-the-config-server)
    - [3. Run with Docker Compose](#3-run-with-docker-compose)
    - [4. Run Individually](#4-run-individually)
- [Service Configuration](#service-configuration)
    - [API Gateway](#api-gateway)
    - [Discovery Server (Eureka)](#discovery-server-eureka)
    - [Config Server](#config-server)
    - [Kafka Setup](#kafka-setup)
    - [Resilience4J (Fault Tolerance)](#resilience4j-fault-tolerance)
    - [Distributed Tracing (Zipkin)](#distributed-tracing-zipkin)
- [Observability](#observability)
    - [Prometheus](#prometheus)
    - [Grafana](#grafana)
- [Kubernetes Deployment](#kubernetes-deployment)
- [CI/CD Pipeline](#cicd-pipeline)
- [API Reference](#api-reference)
- [Environment Variables](#environment-variables)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

This project is a **complete microservices reference implementation** built with Java and the Spring ecosystem. It demonstrates how to build, deploy, and observe a distributed system at scale.

The platform includes everything needed for a production-grade microservices environment:

- Centralized routing via **API Gateway**
- Automatic service registration and discovery via **Eureka**
- Externalized configuration via **Spring Cloud Config Server**
- Asynchronous messaging via **Apache Kafka**
- Fault tolerance patterns (Circuit Breaker, Retry, Rate Limiter) via **Resilience4J**
- End-to-end distributed tracing via **Zipkin**
- Metrics collection and visualization via **Prometheus** and **Grafana**
- Containerization via **Docker** and orchestration via **Kubernetes**
- Automated builds and deployments via **CI/CD (GitHub Actions)**

---

## Architecture

```
                        ┌─────────────────────────────────────────────┐
                        │              CLIENT (Browser / App)          │
                        └──────────────────────┬──────────────────────┘
                                               │
                        ┌──────────────────────▼──────────────────────┐
                        │               API GATEWAY                    │
                        │        (Spring Cloud Gateway - 8080)         │
                        └───────┬───────────────────────┬─────────────┘
                                │                       │
               ┌────────────────▼──────┐   ┌───────────▼────────────────┐
               │    SERVICE A           │   │    SERVICE B               │
               │  (e.g. Order Service) │   │  (e.g. Product Service)    │
               └────────────┬──────────┘   └─────────────┬──────────────┘
                            │                             │
               ┌────────────▼─────────────────────────────▼──────────────┐
               │                   KAFKA MESSAGE BROKER                   │
               └──────────────────────────────────────────────────────────┘

  ┌─────────────────────┐   ┌──────────────────┐   ┌──────────────────────┐
  │   DISCOVERY SERVER  │   │   CONFIG SERVER  │   │   ZIPKIN SERVER      │
  │     (Eureka - 8761) │   │   (8888)         │   │   (9411)             │
  └─────────────────────┘   └──────────────────┘   └──────────────────────┘

  ┌─────────────────────┐   ┌──────────────────┐
  │   PROMETHEUS        │   │   GRAFANA        │
  │   (9090)            │   │   (3000)         │
  └─────────────────────┘   └──────────────────┘
```

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot 3.x |
| Cloud | Spring Cloud 2023.x |
| API Gateway | Spring Cloud Gateway |
| Service Discovery | Netflix Eureka |
| Config Management | Spring Cloud Config |
| Messaging | Apache Kafka |
| Fault Tolerance | Resilience4J |
| Distributed Tracing | Zipkin + Micrometer |
| Monitoring | Prometheus + Grafana |
| Containerization | Docker + Docker Compose |
| Orchestration | Kubernetes (K8s) |
| CI/CD | GitHub Actions |
| Build Tool | Maven |

---

## Services

| Service | Port | Description |
|---|---|---|
| `api-gateway` | 8080 | Entry point for all client requests. Handles routing, load balancing, and security. |
| `discovery-server` | 8761 | Eureka-based service registry. All services register here. |
| `config-server` | 8888 | Centralized configuration server backed by a Git repository. |
| `order-service` | 8081 | Handles order creation and management. |
| `product-service` | 8082 | Manages product catalog and inventory. |
| `notification-service` | 8083 | Consumes events from Kafka and sends notifications. |
| `zipkin` | 9411 | Distributed tracing UI and collector. |
| `prometheus` | 9090 | Metrics collection and storage. |
| `grafana` | 3000 | Metrics visualization and dashboards. |

> **Note:** `order-service`, `product-service`, and `notification-service` are example business microservices. Add or replace with your own domain services.

---

## Prerequisites

Make sure you have the following installed:

- **Java 17+** → [Download](https://adoptium.net/)
- **Maven 3.8+** → [Download](https://maven.apache.org/)
- **Docker & Docker Compose** → [Download](https://www.docker.com/products/docker-desktop/)
- **kubectl** (for Kubernetes) → [Install Guide](https://kubernetes.io/docs/tasks/tools/)
- **Minikube or a K8s cluster** (for local K8s testing) → [Minikube](https://minikube.sigs.k8s.io/docs/)
- **Git**

---

## Project Structure

```
spring-cloud-microservices-platform/
│
├── api-gateway/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── discovery-server/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── config-server/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── product-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── notification-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── config-repo/                    # Git-backed config files
│   ├── application.yml
│   ├── api-gateway.yml
│   ├── order-service.yml
│   └── product-service.yml
│
├── k8s/                            # Kubernetes manifests
│   ├── namespace.yml
│   ├── configmap.yml
│   ├── api-gateway-deployment.yml
│   ├── discovery-server-deployment.yml
│   ├── kafka-deployment.yml
│   ├── zipkin-deployment.yml
│   ├── prometheus-deployment.yml
│   └── grafana-deployment.yml
│
├── monitoring/
│   ├── prometheus.yml
│   └── grafana/
│       └── dashboards/
│
├── docker-compose.yml
├── docker-compose-infra.yml        # Just infra: Kafka, Zipkin, Prometheus, Grafana
├── .github/
│   └── workflows/
│       └── ci-cd.yml
└── pom.xml                         # Parent POM
```

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/spring-cloud-microservices-platform.git
cd spring-cloud-microservices-platform
```

### 2. Configure the Config Server

The Config Server reads from a Git-backed config repository. You can point it to a local folder or a remote repo.

In `config-server/src/main/resources/application.yml`:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: file://${user.home}/config-repo   # or https://github.com/your-org/config-repo
          default-label: main
          clone-on-start: true
```

Create your `config-repo/` directory and add property files per service:

```bash
mkdir -p ~/config-repo
cd ~/config-repo
git init
touch application.yml order-service.yml product-service.yml
git add . && git commit -m "initial config"
```

### 3. Run with Docker Compose

The easiest way to start everything:

```bash
# Start infrastructure (Kafka, Zipkin, Prometheus, Grafana)
docker-compose -f docker-compose-infra.yml up -d

# Build all services
mvn clean package -DskipTests

# Start all services
docker-compose up -d
```

Check all containers are running:

```bash
docker-compose ps
```

### 4. Run Individually

Start services in this order (dependency order):

```bash
# 1. Config Server
cd config-server && mvn spring-boot:run

# 2. Discovery Server
cd discovery-server && mvn spring-boot:run

# 3. Business Microservices
cd order-service && mvn spring-boot:run
cd product-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run

# 4. API Gateway (last)
cd api-gateway && mvn spring-boot:run
```

---

## Service Configuration

### API Gateway

`api-gateway/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: order-service
          uri: lb://ORDER-SERVICE
          predicates:
            - Path=/api/order/**
        - id: product-service
          uri: lb://PRODUCT-SERVICE
          predicates:
            - Path=/api/product/**
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
```

### Discovery Server (Eureka)

`discovery-server/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: discovery-server

server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

Each microservice must include this in their config to register:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Config Server

`config-server/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/<your-org>/config-repo
          default-label: main

server:
  port: 8888
```

Each microservice must bootstrap with the Config Server:

```yaml
# bootstrap.yml in each service
spring:
  application:
    name: order-service
  config:
    import: optional:configserver:http://localhost:8888
```

### Kafka Setup

Add the following to each service that produces or consumes events.

**Producer (e.g. Order Service):**

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest request) {
        // ... business logic
        kafkaTemplate.send("order-placed", new OrderPlacedEvent(order.getId()));
    }
}
```

**Consumer (e.g. Notification Service):**

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: notificationId
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: '*'
```

```java
@Service
@KafkaListener(topics = "order-placed", groupId = "notificationId")
public void listen(OrderPlacedEvent event) {
    log.info("Notification received for order: {}", event.getOrderId());
    // send email/SMS notification
}
```

### Resilience4J (Fault Tolerance)

Add dependency in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

Configuration in `application.yml`:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      product-service:
        registerHealthIndicator: true
        event-consumer-buffer-size: 10
        slidingWindowType: COUNT_BASED
        slidingWindowSize: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 5s
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true

  timelimiter:
    instances:
      product-service:
        timeoutDuration: 3s

  retry:
    instances:
      product-service:
        maxAttempts: 3
        waitDuration: 5s
```

Usage in code:

```java
@CircuitBreaker(name = "product-service", fallbackMethod = "fallbackMethod")
@Retry(name = "product-service")
@TimeLimiter(name = "product-service")
public CompletableFuture<String> getProductDetails(String productId) {
    return CompletableFuture.supplyAsync(() -> productServiceClient.getProduct(productId));
}

public CompletableFuture<String> fallbackMethod(String productId, RuntimeException ex) {
    return CompletableFuture.supplyAsync(() -> "Product service unavailable. Please try later.");
}
```

### Distributed Tracing (Zipkin)

Add dependencies in each service:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

Configure in `application.yml`:

```yaml
management:
  tracing:
    sampling:
      probability: 1.0   # 1.0 = 100% sampling (use lower in production)
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
```

Access Zipkin UI at: [http://localhost:9411](http://localhost:9411)

---

## Observability

### Prometheus

Add Actuator and Prometheus registry to each service:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Expose metrics endpoint in `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, info, metrics, prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

`monitoring/prometheus.yml`:

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'api-gateway'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['api-gateway:8080']

  - job_name: 'order-service'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['order-service:8081']

  - job_name: 'product-service'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['product-service:8082']
```

Access Prometheus UI at: [http://localhost:9090](http://localhost:9090)

### Grafana

Access Grafana at: [http://localhost:3000](http://localhost:3000)

Default credentials: `admin` / `admin`

**Setup steps:**

1. Add Prometheus as a data source: `Configuration → Data Sources → Add → Prometheus → URL: http://prometheus:9090`
2. Import a Spring Boot dashboard: `Dashboards → Import → ID: 11378` (Spring Boot Observability dashboard)
3. For JVM metrics, import dashboard ID: `4701`

---

## Kubernetes Deployment

### Create Namespace

```bash
kubectl apply -f k8s/namespace.yml
```

`k8s/namespace.yml`:

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: microservices
```

### Deploy Services

```bash
kubectl apply -f k8s/ -n microservices
```

Example deployment manifest `k8s/order-service-deployment.yml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order-service
  namespace: microservices
spec:
  replicas: 2
  selector:
    matchLabels:
      app: order-service
  template:
    metadata:
      labels:
        app: order-service
    spec:
      containers:
        - name: order-service
          image: <your-dockerhub-username>/order-service:latest
          ports:
            - containerPort: 8081
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: docker
            - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
              value: http://discovery-server:8761/eureka/
---
apiVersion: v1
kind: Service
metadata:
  name: order-service
  namespace: microservices
spec:
  selector:
    app: order-service
  ports:
    - protocol: TCP
      port: 8081
      targetPort: 8081
```

### Check Status

```bash
kubectl get pods -n microservices
kubectl get services -n microservices
kubectl logs -f <pod-name> -n microservices
```

---

## CI/CD Pipeline

The project uses **GitHub Actions** for automated build, test, and deployment.

`.github/workflows/ci-cd.yml`:

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: Run Tests
        run: mvn test

  docker-build-push:
    needs: build-and-test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Log in to Docker Hub
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and Push Docker Images
        run: |
          services=("api-gateway" "discovery-server" "config-server" "order-service" "product-service" "notification-service")
          for service in "${services[@]}"; do
            docker build -t ${{ secrets.DOCKER_USERNAME }}/$service:latest ./$service
            docker push ${{ secrets.DOCKER_USERNAME }}/$service:latest
          done

  deploy-to-k8s:
    needs: docker-build-push
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Set up kubectl
        uses: azure/setup-kubectl@v3

      - name: Deploy to Kubernetes
        run: kubectl apply -f k8s/ -n microservices
```

> Add `DOCKER_USERNAME` and `DOCKER_PASSWORD` as GitHub Secrets under `Settings → Secrets and Variables → Actions`.

---

## API Reference

All requests go through the API Gateway at `http://localhost:8080`.

| Method | Endpoint | Service | Description |
|---|---|---|---|
| `GET` | `/api/product` | product-service | Get all products |
| `POST` | `/api/product` | product-service | Create a product |
| `GET` | `/api/product/{id}` | product-service | Get product by ID |
| `POST` | `/api/order` | order-service | Place a new order |
| `GET` | `/api/order/{id}` | order-service | Get order by ID |
| `GET` | `/actuator/health` | any service | Health check |
| `GET` | `/actuator/prometheus` | any service | Prometheus metrics |

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `default` | Active Spring profile (`docker`, `k8s`, `prod`) |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | `http://localhost:8761/eureka/` | Eureka server URL |
| `SPRING_CONFIG_IMPORT` | `configserver:http://localhost:8888` | Config server URL |
| `SPRING_KAFKA_BOOTSTRAPSERVERS` | `localhost:9092` | Kafka broker URL |
| `MANAGEMENT_ZIPKIN_TRACING_ENDPOINT` | `http://localhost:9411/api/v2/spans` | Zipkin endpoint |

---

## Service URLs Quick Reference

| Service | URL |
|---|---|
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| Config Server | http://localhost:8888 |
| Zipkin UI | http://localhost:9411 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |
| Kafka (internal) | localhost:9092 |

---

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -m 'Add my feature'`
4. Push to the branch: `git push origin feature/my-feature`
5. Open a Pull Request

Please make sure your code passes all tests before submitting a PR:

```bash
mvn clean test
```

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

<div align="center">

Built with ❤️ using Spring Boot & Spring Cloud

⭐ Star this repo if you find it useful!

</div>