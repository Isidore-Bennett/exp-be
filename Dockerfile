FROM openjdk:8-alpine as builder

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle/ gradle/

COPY src/ src/

RUN ./gradlew clean build -x test

FROM openjdk:8-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/demo1-0.0.1-SNAPSHOT-plain.jar app.jar

EXPOSE 12345

ENTRYPOINT ["java", "-jar", "app.jar"]