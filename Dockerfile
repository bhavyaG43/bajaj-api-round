# ── Stage 1: Build ────────────────────────────────────────────────────────────
# Use a full JDK image to compile and package the application
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom first (layer-cached; only re-runs when pom changes)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B || true

# Copy source and build the fat JAR (skip tests — they run in CI/CD, not here)
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ── Stage 2: Run ──────────────────────────────────────────────────────────────
# Use a slim JRE-only image — much smaller final image (~100 MB vs ~400 MB)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the fat JAR from the builder stage
COPY --from=builder /app/target/bajaj-finserv-api-1.0.0.jar app.jar

# Render injects PORT env var; the app reads it via server.port=${PORT:8080}
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
