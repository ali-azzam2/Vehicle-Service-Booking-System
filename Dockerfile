# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/vehicle-service-booking-1.0.0.jar .

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "vehicle-service-booking-1.0.0.jar"] 