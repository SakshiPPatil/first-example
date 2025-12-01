# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]