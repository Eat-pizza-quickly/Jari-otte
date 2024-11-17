FROM openjdk:17-oracle

WORKDIR /app

COPY build/libs/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]