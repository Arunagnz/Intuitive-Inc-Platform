# Stage 1: Build
FROM maven:3.8.6-eclipse-temurin-17 AS build
LABEL authors="arunagirinathan (arun) rajan"
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Package into image
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /app/target/*.jar /app/product-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]
