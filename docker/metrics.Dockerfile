FROM maven:3.8.5-openjdk-17 AS build

COPY ../pom.xml /build/
COPY ../query-sandbox-lib /build/query-sandbox-lib
COPY ../query-sandbox-metrics /build/query-sandbox-metrics

WORKDIR /build/
RUN mvn clean install -Pmetrics -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=build /build/query-sandbox-metrics/target/query-sandbox-metrics-0.0.1-SNAPSHOT.jar metrics.jar
RUN chmod +x metrics.jar
ENTRYPOINT ["java", "-jar", "metrics.jar"]
