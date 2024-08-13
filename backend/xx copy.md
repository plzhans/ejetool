


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