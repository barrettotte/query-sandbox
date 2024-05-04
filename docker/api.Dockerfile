FROM maven:3.8.5-openjdk-17 AS build

COPY ../pom.xml /build/
COPY ../query-sandbox-lib /build/query-sandbox-lib
COPY ../query-sandbox-api /build/query-sandbox-api

WORKDIR /build/
RUN mvn -pl query-sandbox-lib,query-sandbox-api clean install

FROM openjdk:17-jdk-alpine
COPY --from=build /build/query-sandbox-api/target/query-sandbox-api-0.0.1-SNAPSHOT.jar api.jar
EXPOSE 8080
RUN chmod +x api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]
