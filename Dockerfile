FROM openjdk:17-ea-4-jdk-slim
WORKDIR /app

# Copy the built JAR file from your host's target directory to the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} dataconsumer-1.0.jar
COPY src/main/resources/topics.properties /app/config/topics.properties
# Expose the port your Spring Boot app listens on (default is 8080)

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "dataconsumer-1.0.jar"]

# docker run -p 8081:8081 dataconsumer:1.0
#docker build -t dataconsumer:1.0 .


#docker run --name DC_API -p 8081:8081 dataconsumer:1.0