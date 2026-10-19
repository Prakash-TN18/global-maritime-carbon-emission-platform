FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/reporting-service-1.0.0.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=oracle"]