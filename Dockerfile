# ── Stage 1: Build ────────────────────────────────────────────────────────────
# Official Maven image already has Maven + JDK 21 — no wrapper needed
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom first so dependency download is cached as a separate layer.
# This layer only re-runs when pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the fat JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Run ──────────────────────────────────────────────────────────────
# Slim JRE-only image — keeps final image small (~100 MB)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/bajaj-finserv-api-1.0.0.jar app.jar

# Render injects PORT env var; app reads it via server.port=${PORT:8080}
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
