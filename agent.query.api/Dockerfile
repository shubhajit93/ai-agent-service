# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the main pom.xml and module poms for dependency caching
COPY pom.xml /app/pom.xml
COPY agent.core/pom.xml /app/agent.core/pom.xml
COPY agent.cmd.api/pom.xml /app/agent.cmd.api/pom.xml
COPY agent.query.api/pom.xml /app/agent.query.api/pom.xml
#COPY agent.query.api/src /app/agent.query.api/src

# Download dependencies separately from source code for caching
RUN mvn -f /app/pom.xml dependency:go-offline

# Copy the entire project source code and build
COPY . /app
RUN mvn -f /app/pom.xml clean package -DskipTests

# Stage 2: Run the application
#FROM eclipse-temurin:21-jdk-slim
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY agent.query.api/src/main/resources/application-test.properties /app/application-test.properties
COPY --from=build /app/agent.query.api/target/agent.query.api-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
