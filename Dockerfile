FROM alpine:edge
RUN apk add --no-cache openjdk8
VOLUME /tmp
EXPOSE 8081
ADD /target/yeti-integration-0.0.1-SNAPSHOT.jar yeti-integration-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","yeti-integration-0.0.1-SNAPSHOT.jar"]