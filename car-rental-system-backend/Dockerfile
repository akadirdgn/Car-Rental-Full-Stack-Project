# Backend Dockerfile
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

COPY src ./src
# Build the application using offline dependencies
RUN mvn clean package -DskipTests -o -B

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/car-rental-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
