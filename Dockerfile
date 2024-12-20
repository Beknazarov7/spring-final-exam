# Step 1: Use Maven to build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application and package it as a jar file
RUN mvn clean package -DskipTests

# Step 2: Use a lightweight Java 21 runtime image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
