FROM gradle:8.1.1-jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17.0.7_7-jre-alpine

EXPOSE 8090

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/gateway-microservice.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/gateway-microservice.jar"]