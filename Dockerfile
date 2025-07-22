FROM maven:3.9.8-eclipse-temurin-22-alpine as dev-build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src /build/src
RUN ["mvn","clean","package","-DskipTests"]

FROM eclipse-temurin:22.0.2_9-jdk-alpine
COPY --from=dev-build /build/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
CMD java -jar app.jar