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

RUN mkdir -p /app/config && mkdir -p /app/keys
WORKDIR /app

EXPOSE 8080

ENTRYPOINT [ "java" ]
