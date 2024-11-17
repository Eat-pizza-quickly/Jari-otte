FROM openjdk:17-oracle

WORKDIR /app

COPY build/libs/concert-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]