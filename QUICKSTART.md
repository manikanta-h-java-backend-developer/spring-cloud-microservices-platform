# 🚀 Quick Start Guide - Complete Step-by-Step

This guide walks you through **running the entire microservices platform** from scratch, with exact file names and commands.

---

## 📋 Prerequisites Checklist

Before you start, ensure you have installed:

- [ ] **Java 17+** → [Download from adoptium.net](https://adoptium.net/)
- [ ] **Maven 3.8+** → [Download from maven.apache.org](https://maven.apache.org/)
- [ ] **Docker & Docker Compose** → [Download from docker.com](https://www.docker.com/products/docker-desktop/)
- [ ] **Git** → [Download from git-scm.com](https://git-scm.com/)

### Verify Installation

Open PowerShell and run:

```powershell
java -version
mvn -version
docker --version
docker-compose --version
git --version
```

If any command fails, install the missing tool.

---

## 🏗️ Project Structure Overview

Your project directory looks like this:

```
spring-cloud-microservices-platform/
├── api-gateway/                    # API Gateway (Port 8080)
├── discovery-server/               # Eureka Server (Port 8761)
├── config-server/                  # Config Server (Port 8888)
├── order-service/                  # Order Service (Port 8081)
├── product-service/                # Product Service (Port 8082)
├── notification-service/           # Notification Service (Port 8083)
├── config-repo/                    # Git-backed configuration files
├── k8s/                            # Kubernetes manifests
├── monitoring/                     # Prometheus config
├── docker-compose.yml              # Complete stack (all services)
├── docker-compose-infra.yml        # Infrastructure only
├── mvnw & mvnw.cmd                 # Maven Wrapper (use instead of `mvn`)
└── pom.xml                         # Parent POM (module aggregator)
```

---

## 🎯 Running the Application - Two Approaches

### **Approach 1: Using Docker Compose (Recommended - Fastest)**

This runs everything in containers automatically.

#### Step 1: Navigate to Project Root

```powershell
cd D:\Intelij-Projects\High-Performance Spring Boot Microservices Architecture\spring-cloud-microservices-platform
```

#### Step 2: Build All Services

```powershell
.\mvn clean package -DskipTests
```

**What happens:**
- Compiles all modules (api-gateway, discovery-server, config-server, order-service, product-service, notification-service)
- Packages them as JAR files
- Downloads all dependencies (might take 2-5 minutes on first run)

✅ **Success indicator:** You should see `BUILD SUCCESS` at the end.

#### Step 3: Start Infrastructure (Kafka, Zookeeper, Zipkin, Prometheus, Grafana)

```powershell
docker-compose -f docker-compose-infra.yml up -d
```

**What this does:**
- Starts Zookeeper (Port 2181)
- Starts Kafka (Port 9092)
- Starts Zipkin (Port 9411)
- Starts Prometheus (Port 9090)
- Starts Grafana (Port 3000)

**Verify it's running:**

```powershell
docker-compose -f docker-compose-infra.yml ps
```

#### Step 4: Start All Microservices (Complete Stack)

```powershell
docker-compose up -d
```

**What this does:**
- Builds Docker images for all 6 services
- Starts Discovery Server (Eureka) on Port 8761
- Starts Config Server on Port 8888
- Starts Order Service on Port 8081
- Starts Product Service on Port 8082
- Starts Notification Service on Port 8083
- Starts API Gateway on Port 8080
- Plus all infrastructure services

**Verify all containers are running:**

```powershell
docker-compose ps
```

You should see 12 services running (6 app services + 6 infra services).

#### Step 5: Monitor Logs

```powershell
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f api-gateway
docker-compose logs -f order-service
docker-compose logs -f discovery-server
```

---

### **Approach 2: Running Services Individually (Manual - For Development)**

This is useful if you want to debug or modify code.

#### Step 1: Navigate to Project Root

```powershell
cd D:\Intelij-Projects\High-Performance Spring Boot Microservices Architecture\spring-cloud-microservices-platform
```

#### Step 2: Start Infrastructure Containers Only

```powershell
docker-compose -f docker-compose-infra.yml up -d
```

#### Step 3: Start Services in Order (Dependency Order)

**Terminal 1 - Discovery Server:**

```powershell
cd discovery-server
.\mvnw.cmd spring-boot:run
```

Wait until you see: `Eureka Server is now ready to serve traffic`

**Terminal 2 - Config Server:**

```powershell
cd config-server
.\mvnw.cmd spring-boot:run
```

Wait until you see: `onfigServer started on port 8888`

**Terminal 3 - Order Service:**

Open a new terminal, then:

```powershell
cd order-service
.\mvnw.cmd spring-boot:run
```

Wait until you see: `Started OrderServiceApplication`

**Terminal 4 - Product Service:**

```powershell
cd product-service
.\mvnw.cmd spring-boot:run
```

**Terminal 5 - Notification Service:**

```powershell
cd notification-service
.\mvnw.cmd spring-boot:run
```

**Terminal 6 - API Gateway:**

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

Once all services are started, you should see them registered in Eureka.

---

## 🌐 Accessing Services & Dashboards

Once everything is running, open these URLs in your browser:

| Service | URL | Purpose |
|---|---|---|
| **API Gateway** | http://localhost:8080 | Entry point for requests |
| **Eureka Dashboard** | http://localhost:8761 | See registered services |
| **Config Server** | http://localhost:8888 | Centralized configuration |
| **Zipkin UI** | http://localhost:9411 | Distributed tracing & request flow |
| **Prometheus** | http://localhost:9090 | Metrics collection |
| **Grafana** | http://localhost:3000 | Dashboards & visualization |

---

## 📡 Testing the Services

### Test 1: Check Service Registration in Eureka

```powershell
# Open this URL to see all registered services
http://localhost:8761
```

You should see 5 services registered:
- `API-GATEWAY`
- `ORDER-SERVICE`
- `PRODUCT-SERVICE`
- `NOTIFICATION-SERVICE`
- `CONFIG-SERVER`

### Test 2: Call Order Service via API Gateway

```powershell
$uri = "http://localhost:8080/api/order"
$body = @{
    orderId = "ORD-001"
    productId = "PROD-101"
    quantity = 2
    customerName = "John Doe"
} | ConvertTo-Json

Invoke-WebRequest -Uri $uri -Method POST -Body $body -ContentType "application/json"
```

**Expected Response:** Order created successfully with a unique order ID.

### Test 3: Call Product Service via API Gateway

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/product" -Method GET
```

**Expected Response:** List of products in JSON format.

### Test 4: Check Application Health

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -Method GET
```

**Expected Response:** `{"status":"UP"}`

---

## 🔍 Viewing Logs & Debugging

### View Logs Using Docker Compose

```powershell
# All logs (live streaming)
docker-compose logs -f

# Last 50 lines of all services
docker-compose logs --tail=50

# Specific service logs
docker-compose logs -f api-gateway
docker-compose logs -f discovery-server
docker-compose logs -f config-server
```

### View Container Details

```powershell
# See which containers are running
docker-compose ps

# Inspect a specific container (e.g., order-service)
docker inspect order-service

# Execute command inside a running container
docker-compose exec order-service /bin/sh
```

---

## 🛑 Stop Running Application (App + Containers)

Use these commands from project root:
`D:\Intelij-Projects\High-Performance Spring Boot Microservices Architecture\spring-cloud-microservices-platform`

### 1) Stop Services Started in Manual Mode (non-Docker)

If you started services with `spring-boot:run`, stop each terminal with `Ctrl + C`.

If a Java service is still running in background, find and stop it:

```powershell
# list Java processes
Get-Process java

# stop a specific process
Stop-Process -Id <PID> -Force
```

### 2) Stop Containers Started by Compose (keep data/volumes)

```powershell
docker compose -f docker-compose.yml down
docker compose -f docker-compose-infra.yml down
```

### 3) Stop Containers + Remove Volumes (full reset)

```powershell
docker compose -f docker-compose.yml down -v --remove-orphans
docker compose -f docker-compose-infra.yml down -v --remove-orphans
```

### 4) Emergency: Stop All Running Docker Containers on your machine

```powershell
$ids = docker ps -q
if ($ids) { docker stop $ids } else { Write-Host "No running containers." }
```

---

## 🧹 Remove Containers and Images Completely

### 1) Remove Compose-created images for this project

```powershell
docker compose -f docker-compose.yml down --rmi local
docker compose -f docker-compose-infra.yml down --rmi local
```

### 2) Remove specific infrastructure images used by this project

```powershell
docker image rm -f confluentinc/cp-zookeeper:7.6.1
docker image rm -f confluentinc/cp-kafka:7.6.1
docker image rm -f openzipkin/zipkin:latest
docker image rm -f prom/prometheus:latest
docker image rm -f grafana/grafana:latest
```

### 3) Optional: remove dangling/unused images system-wide

```powershell
docker image prune -a -f
```

### 4) Verify everything is stopped/removed

```powershell
docker ps
docker ps -a
docker images
```

### 5) Clean Maven build artifacts

```powershell
.\mvnw.cmd clean
```

### 6) Reclaim Docker/WSL memory immediately (if RAM still looks high)

```powershell
docker system df
docker system prune -a --volumes -f
wsl --shutdown
```

After `wsl --shutdown`, reopen Docker Desktop.

---

## 🐛 Troubleshooting

### Issue 1: Maven Build Fails

**Error:** `BUILD FAILURE`

**Solution:**
```powershell
# Ensure Java 17 is installed
java -version

# Set JAVA_HOME environment variable
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17.0.x"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Try building again
.\mvnw.cmd clean package -DskipTests
```

### Issue 2: Port Already in Use

**Error:** `Address already in use` or `Port 8080 is already in use`

**Solution:**
```powershell
# Find process using the port (e.g., 8080)
netstat -ano | findstr :8080

# Kill the process (replace PID with the actual process ID)
taskkill /PID <PID> /F
```

### Issue 3: Containers Won't Start

**Error:** `Cannot start service ... driver failed`

**Solution:**
```powershell
# Restart Docker Desktop (from system tray)
# Or use PowerShell:
Restart-Service -Name "Docker Desktop"

# Or manually:
docker-compose down
docker-compose up -d
```

### Issue 4: Microservice Can't Connect to Eureka

**Error:** `Cannot connect to Eureka` or `DiscoveryClient registration status: DOWN`

**Solution:**
- Ensure Discovery Server is running before other services
- Check that Eureka is accessible: `http://localhost:8761`
- Verify network connectivity: `docker network ls`

### Issue 5: Config Server Can't Find Configuration Files

**Error:** `Cannot locate property source: configserver`

**Solution:**
- Ensure `config-repo/` folder exists
- Check that config files exist:
  - `config-repo/application.yml`
  - `config-repo/order-service.yml`
  - `config-repo/product-service.yml`
- Restart config-server after adding configs

---

## 📊 Key Configuration Files

### Root POM - `pom.xml`

**Defines:**
- Module list (6 microservices)
- Java version (17)
- Spring Cloud version (2023.0.3)
- Dependencies for all services

**Edit if:** You need to add new modules or change versions.

### Docker Compose - `docker-compose.yml`

**Defines:**
- Service definitions
- Port mappings
- Dependencies between services
- Environment variables

**Edit if:** You need to change ports or add new services.

### Infrastructure Config - `docker-compose-infra.yml`

**Defines:**
- Zookeeper, Kafka, Zipkin, Prometheus, Grafana

**Use for:** Testing without all microservices.

### Prometheus Config - `monitoring/prometheus.yml`

**Defines:**
- Metrics scrape targets
- Scrape intervals

**Edit if:** You add new services that need monitoring.

### Kubernetes Manifests - `k8s/`

**Contains:**
- `namespace.yml` - Kubernetes namespace
- `configmap.yml` - Config maps
- Service deployments (one per microservice)
- StatefulSets for infrastructure

**Use for:** Deploying to Kubernetes.

---

## 🎓 What Each Service Does

| Service | Port | Purpose | Config File |
|---|---|---|---|
| **api-gateway** | 8080 | Routes all requests to backend services | `api-gateway/src/main/resources/application.yml` |
| **discovery-server** | 8761 | Service registry (Eureka) | `discovery-server/src/main/resources/application.yml` |
| **config-server** | 8888 | Centralized config distribution | `config-server/src/main/resources/application.yml` |
| **order-service** | 8081 | Creates/manages orders, publishes events to Kafka | `config-repo/order-service.yml` |
| **product-service** | 8082 | Manages product catalog | `config-repo/product-service.yml` |
| **notification-service** | 8083 | Listens to Kafka events, sends notifications | `config-repo/notification-service.yml` |

---

## ✅ Success Checklist

After completing the setup, verify:

- [ ] All 6 microservices are running
- [ ] All infrastructure services are running (Kafka, Zookeeper, Zipkin, Prometheus, Grafana)
- [ ] Eureka dashboard shows 5+ services registered
- [ ] Can make a POST request to create an order
- [ ] Can make a GET request to list products
- [ ] Zipkin shows distributed traces
- [ ] Prometheus shows metrics
- [ ] Can access Grafana at `http://localhost:3000` (default: admin/admin)

---

## 📚 Next Steps

Once everything is running:

1. **Modify code** in any service's `src/` folder
2. **Rebuild services:** `.\mvnw.cmd clean package -DskipTests`
3. **Update containers:** `docker-compose up -d --build`
4. **View logs:** `docker-compose logs -f <service-name>`

For **Kubernetes deployment**, see the main [README.md](README.md).

---

## 🆘 Need Help?

1. Check logs: `docker-compose logs -f`
2. Verify ports are accessible
3. Ensure Docker Desktop is running
4. Check Java/Maven versions match prerequisites
5. Review service startup order in the README

---

Happy coding! 🎉


