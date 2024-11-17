FROM openjdk:17-oracle

WORKDIR /app

COPY build/libs/notification-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]