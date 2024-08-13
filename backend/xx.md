


spring init --build=gradle --type=gradle-project \
--packaging=jar \
--groupId=com.ejetool \
--artifactId=common \
--name=ejetool-common \
./lib/ejetool-common/


spring init --build=gradle --type=gradle-project \
--packaging=jar \
--groupId=com.ejetool.openai \
--artifactId=api \
--name=ejetool-openai-api \
./openai/ejetool-openai-api/

spring init --build=gradle --type=gradle-project \
--packaging=jar \
--groupId=com.ejetool.openai \
--artifactId=service \
--name=ejetool-openai-service \
./openai/ejetool-openai-service/

spring init --build=gradle --type=gradle-project \
--packaging=jar \
--groupId=com.ejetool.openai \
--artifactId=client \
--name=ejetool-openai-client \
./openai/ejetool-openai-client/


spring init --build=gradle --type=gradle-project \
--packaging=jar \
--groupId=com.ejetool.videoai \
--artifactId=videoai \
--name=ejetool-videoai-videoai \
./videoai/ejetool-videoai-videoai/




docker run --name xx1234 \
        -it \
        --privileged \
        --dns 8.8.8.8 \
        --rm \
        --entrypoint "" \
        -v $(pwd)/master/config:/app/config \
        ghcr.io/plzhans/ejetool/ejetool-videoai-chatbot:master /bin/sh


docker run --name xx1234 \
        -it \
        --privileged \
        --dns 8.8.8.8 \
        --rm \
        --entrypoint "" \
        -v /volume3/docker/github-runner-ejetool-develop/ejetool/ejetool/master/config:/app/config \
        ghcr.io/plzhans/ejetool/ejetool-videoai-chatbot:master /bin/sh
