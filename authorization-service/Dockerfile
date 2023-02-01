FROM amazoncorretto:11 AS builder
WORKDIR /tt
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./
RUN ./gradlew --no-daemon dependencies
COPY src/ src/
RUN ./gradlew --no-daemon build
FROM amazoncorretto:11
WORKDIR /tt
EXPOSE 8080
CMD ["java", "-jar", "/tt/authorization.jar"]
COPY --from=builder /tt/build/libs/authorization-*.jar authorization.jar