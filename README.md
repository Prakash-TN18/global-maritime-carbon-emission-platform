# Global Maritime Carbon Emission Management Platform

Portfolio-ready Java 17 and Spring Boot multi-module project with four independently runnable microservices.

## Services
- Shipment Service `8081`: shipment CRUD
- Route Service `8082`: port-pair distance master
- Emission Service `8083`: calls shipment and route services and calculates `distanceKm * weightTons * factor`
- Reporting Service `8084`: emission summary and shipment report

## Run locally
```bash
mvn clean package
java -jar shipment-service/target/shipment-service-1.0.0.jar
java -jar route-service/target/route-service-1.0.0.jar
java -jar emission-service/target/emission-service-1.0.0.jar
java -jar reporting-service/target/reporting-service-1.0.0.jar
```
Default database is H2 for easy demonstration. For Oracle, create the tables using `database/oracle-schema.sql`, set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and run with `--spring.profiles.active=oracle`.

## Demo flow
1. POST shipment to `http://localhost:8081/api/shipments`
2. POST route to `http://localhost:8082/api/routes`
3. POST calculation to `http://localhost:8083/api/emissions/calculate`
4. GET summary from `http://localhost:8084/api/reports/summary`

Import `postman/GMCEMP.postman_collection.json` into Postman for ready-made requests.
