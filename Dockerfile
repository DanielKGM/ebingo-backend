FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/ebingo-0.0.1-SNAPSHOT.jar ebingo.jar

ENV SPRING_PROFILES_ACTIVE=docker
ENV JWT_SECRET=chaveultrasecreta

ENTRYPOINT ["java", "-jar", "/app/ebingo.jar"]

EXPOSE 8080
