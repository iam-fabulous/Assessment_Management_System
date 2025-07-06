# --- Build stage with Java 23 ---
FROM eclipse-temurin:23-jdk AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# --- Run stage with Java 23 ---
FROM eclipse-temurin:23-jre
WORKDIR /usr/app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]







#FROM maven:3.8.7 AS build
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:go-offline
#COPY src ./src
#RUN mvn clean package -DskipTests
## ---- Run Stage ----
#FROM openjdk:17-alpine
#WORKDIR /usr/app
#COPY --from=build /app/target/*.jar app.jar
## Expose port 8080
#EXPOSE 8080
## Run the app
#ENTRYPOINT ["java", "-jar", "app.jar"]
