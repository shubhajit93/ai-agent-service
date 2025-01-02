# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the main pom.xml and module poms for dependency caching
COPY pom.xml /app/pom.xml
COPY agent.core/pom.xml /app/agent.core/pom.xml
COPY agent.cmd.api/pom.xml /app/agent.cmd.api/pom.xml
COPY agent.query.api/pom.xml /app/agent.query.api/pom.xml

# Download dependencies separately from source code for caching
RUN mvn -f /app/pom.xml dependency:go-offline

# Copy the entire project source code and build
#COPY . /app
COPY agent.core /app/agent.core
COPY agent.cmd.api /app/agent.cmd.api
COPY agent.query.api /app/agent.query.api

# Install parent POM
RUN mvn install -N -DskipTests

# Build and install agent.core
RUN mvn -pl agent.core clean install -DskipTests

# Build the application
RUN mvn -pl agent.cmd.api clean package -DskipTests
#RUN mvn -f /app/pom.xml clean package -DskipTests

# Stage 2: Run the application
#FROM openjdk:21-jdk-slim
#WORKDIR /app
#COPY --from=build /app/agent.cmd.api/target/agent.cmd.api-1.0.0.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Use slim JDK for runtime
FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app

# Copy the packaged application
COPY --from=build /app/agent.cmd.api/target/agent.cmd.api-1.0.0.jar /app/app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
