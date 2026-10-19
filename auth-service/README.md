# Auth Service
Add `<module>auth-service</module>` to the root pom.xml modules list.

Build from project root: `mvn clean package`
Run: `java -jar auth-service\target\auth-service-1.0.0.jar`

POST `/api/auth/register` body: `{"username":"prakash","password":"Password@123","role":"ADMIN"}`
POST `/api/auth/login` body: `{"username":"prakash","password":"Password@123"}`

Set a strong secret outside source code before production use: `set JWT_SECRET=<at-least-32-byte-random-secret>`.
