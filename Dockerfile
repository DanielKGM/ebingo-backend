FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/ebingo-0.0.1-SNAPSHOT.jar /app/ebingo.jar

ENTRYPOINT ["java", "-jar", "ebingo.jar"]

EXPOSE 8080
