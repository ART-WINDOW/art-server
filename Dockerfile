FROM openjdk:21-jdk-slim-buster
WORKDIR /app
COPY /build/libs/art-server-0.0.1-SNAPSHOT.jar .
ENTRYPOINT java -jar art-server-0.0.1-SNAPSHOT.jar