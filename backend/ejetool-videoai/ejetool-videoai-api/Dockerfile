ARG JAVA_DISTRIBUTION=eclipse-temurin
ARG JAVA_VERSION=22

FROM ${JAVA_DISTRIBUTION}:${JAVA_VERSION}-jre-alpine

RUN apk update && apk add --no-cache \
    net-tools \
    curl wget \
    vim \
    openssl \
    unzip \
    jq

ENV SPRING_PROFILES_ACTIVE=production

RUN mkdir -p /app/config && mkdir -p /app/keys
WORKDIR /app

COPY ./builds/ejetool-videoai-api.jar ./bin/ejetool-videoai-api.jar

EXPOSE 8080

ENTRYPOINT [ "java" ]
CMD ["-jar","./bin/ejetool-videoai-api.jar"]

# ./gradlew build -x test -i -Prelease -DoutputDir=./builds

# docker build -f ./ejetool-videoai/ejetool-videoai-api/Dockerfile -t test1234  .

# docker run -v $(pwd)/ejetool-videoai/ejetool-videoai-api/src/main/resources/application-local.yml:/app/config/application-dev.yml --entrypoint "" --rm -it test1234 /bin/sh
# docker run -v $(pwd)/ejetool-videoai/ejetool-videoai-api/src/main/resources/application-local.yml:/app/config/application-production.yml --rm test1234


# curl -H "Authorization: token YOUR_TOKEN" -o 파일명.ext https://raw.githubusercontent.com/사용자명/저장소명/브랜치명/파일경로/파일명.ext
