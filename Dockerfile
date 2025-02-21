FROM maven:3.8.6-openjdk-18-slim AS builder
WORKDIR /app
COPY Backend/pom.xml .
COPY Backend/src ./src
RUN mvn clean package -DskipTests
FROM openjdk:19-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]