FROM eclipse-temurin:17-jdk as BUILDER
COPY . /opt/app
WORKDIR /opt/app
RUN ./gradlew build

FROM gcr.io/distroless/java17-debian11
COPY --from=BUILDER /opt/app/build/libs/backend.jar /opt/app/backend.jar
WORKDIR /opt/app
ENTRYPOINT ["java", "-jar", "backend.jar", "--spring.profiles.active=prd"]
