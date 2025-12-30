FROM amazoncorretto:17
WORKDIR /app
# Copy the JAR file built locally by 'mvn clean package'
COPY target/car-rental-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
