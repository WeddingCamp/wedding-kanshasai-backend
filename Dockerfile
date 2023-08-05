FROM openjdk:17-jdk-slim-bullseye as BUILDER

COPY . /opt/app
WORKDIR /opt/app
RUN ./gradlew build

FROM gcr.io/distroless/java17-debian11
COPY --from=BUILDER /opt/app/build/libs/backend.jar /opt/app/backend.jar
WORKDIR /opt/app
CMD ["java", "-jar", "backend.jar"]
