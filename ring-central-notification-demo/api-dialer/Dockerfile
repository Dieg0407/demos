FROM gradle:7.4.2-jdk11 as build

WORKDIR /tmp

COPY src ./src
COPY build.gradle ./
COPY settings.gradle ./

RUN gradle bootJar

FROM adoptopenjdk:11-jre-hotspot-focal as runtime

WORKDIR /app

COPY --from=build /tmp/build/libs/*.jar api.jar

ENTRYPOINT java -jar api.jar
