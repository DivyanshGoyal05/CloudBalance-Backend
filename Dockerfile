# ---- Build stage ----
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY ../../ /app

# Move to Gradle project dir
WORKDIR /app/Backend/CloudBalance-Backend

# Ensure Gradle wrapper is executable (Linux)
RUN chmod +x gradlew

# Optional diagnostics
RUN ./gradlew --version

# Verify DTO exists (helps catch missing file in build context)
RUN test -f src/main/java/com/example/cloudbalanced/dto/RegisterRequest.java || (echo "Missing RegisterRequest.java" && ls -la src/main/java/com/example/cloudbalanced/dto && exit 1)

# Build (skip tests for faster image on CI; adjust as needed)
RUN --mount=type=cache,id=gradle-cache,target=/root/.gradle \
    ./gradlew clean build -x test -x check --stacktrace --info

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/Backend/CloudBalance-Backend/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]


