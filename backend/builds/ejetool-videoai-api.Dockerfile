ARG JAVA_DISTRIBUTION=eclipse-temurin
ARG JAVA_VERSION=22

FROM ${JAVA_DISTRIBUTION}:${JAVA_VERSION}-jre

RUN mkdir app 
WORKDIR /app
COPY ./builds/ejetool-videoai-api-0.0.1-SNAPSHOT.jar /app/bin/ejetool-videoai-api.jar
COPY ./infra/keys/serivce_auth.pub /app/keys/

ENV SPRING_PROFILES_ACTIVE=production

ENTRYPOINT [ "java" ]
CMD ["-jar","/app/bin/ejetool-videoai-api.jar"]

# ./gradlew build -x test -i -Prelease -DoutputDir=./builds

# docker build -f ./ejetool-videoai/ejetool-videoai-api/Dockerfile -t test1234  .

# docker run -v $(pwd)/ejetool-videoai/ejetool-videoai-api/src/main/resources/application-dev.yml:/app/config/application-dev.yml --entrypoint "" --rm -it test1234 /bin/bash
# docker run -v $(pwd)/ejetool-videoai/ejetool-videoai-api/src/main/resources/application-dev.yml:/app/config/application-production.yml --rm test1234