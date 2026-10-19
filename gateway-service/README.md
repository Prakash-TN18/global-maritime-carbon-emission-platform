1. Copy gateway-service into GMCEMP root project.
2. Add <module>gateway-service</module> to root pom.xml.
3. Build: mvn clean package
4. Start: java -jar gateway-service/target/gateway-service-1.0.0.jar
5. Access through Gateway:
   http://localhost:8080/api/auth/**
   http://localhost:8080/api/shipments/**
   http://localhost:8080/api/routes/**
   http://localhost:8080/api/emissions/**
   http://localhost:8080/api/reports/**
