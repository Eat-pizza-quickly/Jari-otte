FROM openjdk:17-oracle

WORKDIR /app

COPY build/libs/coupon-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]