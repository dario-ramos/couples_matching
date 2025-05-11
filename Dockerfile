# === Build stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# === Runtime stage ===
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/couples-matching-1.0.0-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
