
# ejetool backend


[Video AI](#video-ai) : 플랫폼을 이용해서 키워드로 [Youtube 영상](https://www.youtube.com/channel/UC9wnpGyTlZdp3WSEgtySBIw) 자동 생성

## 목차
* [공통](#common)
* [Video AI](#video-ai)

## <a id="common"></a>공통

### 소스
* [ejetool-core](./ejetool-core)
* [ejetool-lib](./ejetool-lib)
  * [ejetool-lib-creatomate](./ejetool-lib/ejetool-lib-creatomate)
  * [ejetool-lib-openai](./ejetool-lib/ejetool-lib-openai)
  * [ejetool-lib-speech](./ejetool-lib/ejetool-lib-speech)
  * [ejetool-lib-telegram](./ejetool-lib/ejetool-lib-telegram)
  * [ejetool-lib-useapi](./ejetool-lib/ejetool-lib-useapi)
  * [ejetool-lib-youtube](./ejetool-lib/ejetool-lib-youtube)

## <a id="video-ai"></a>
### 주요 기능

- 키워드로 자동 [Youtube 영상](https://www.youtube.com/channel/UC9wnpGyTlZdp3WSEgtySBIw) 만들기
- ~~커뮤니티 사이트의 내용으로 영상 만들기~~(준비중)

### 프로세스
- 주제 지시 : 텍스트 또는 뉴스, 커뮤니티등 URL
- 주제 생성 및 분석 : [OpenAI](https://platform.openai.com/)
- 관련 이미지 생성 : [Midjourney](https://midjourney.com)
- 관련 음성 생성 : [Google text to speech](https://cloud.google.com/text-to-speech)
- 관련 영상 생성 : [Creatomate](https://creatomate.com)
- Youtube 영상 등록 : [Youtube API](https://developers.google.com/youtube) or [Make.com](https://www.make.com/)(module [Youtube](https://www.make.com/en/help/app/youtube))
  - 일반적인 방법으로 Youtube 등록할 경우 [daily quota](https://developers.google.com/youtube/v3/guides/quota_and_compliance_audits) 걸림 (하루 7개 정도)
  - 일단 quota 한도 우회하기 위해 Make.com 의 Youtube 모듈 이용
  - google project별 한도에 걸리는데 make.com 에는 Quota 제한이 큰거 같음
- 알림 및 커맨드 : [Telegram bot](https://core.telegram.org/bots/api)

### 인프라
- Mysql : Database
- Redis : Cache
- Redis stream : Kafka 대신 Message queue 사용(경량 서비스에 적합)

### <a id="video-ai-demo"></a>Demo

Making sample
> [![Everything Is AWESOME](https://img.youtube.com/vi/918-KiaatKU/0.jpg)](https://www.youtube.com/watch?v=918-KiaatKU "Everything Is AWESOME")

Youtube shorts : https://www.youtube.com/shorts/f1cBTgdlpB0
> [![Everything Is AWESOME](https://img.youtube.com/vi/f1cBTgdlpB0/0.jpg)](https://www.youtube.com/shorts/f1cBTgdlpB0 "Everything Is AWESOME")

### 소스
* [ejetool-videoai](./ejetool-videoai)
  * [ejetool-videoai-api](./ejetool-videoai/ejetool-videoai-api)
  * [ejetool-videoai-chatbot](./ejetool-videoai/ejetool-videoai-chatbot)
  * [ejetool-videoai-common](./ejetool-videoai/ejetool-videoai-common)
  * [ejetool-videoai-domain](./ejetool-videoai/ejetool-videoai-domain)
  * [ejetool-videoai-event-consumer](./ejetool-videoai/ejetool-videoai-event-consumer)
  * [ejetool-videoai-event-publisher](./ejetool-videoai/ejetool-videoai-event-publisher)
  * [ejetool-videoai-service](./ejetool-videoai/ejetool-videoai-service)

### 빌드
build
```
./gradlew build
```

run
```
# videoai-chatbot
./gradlew ejetool-videoai-chatbot:bootRun

# videoai-api
./gradlew ejetool-videoai-api:bootRun

# videoai-event-consumer 
./gradlew ejetool-videoai-event-consumer:bootRun
```

### 해야할 일
모듈
- useapi를 대체하기 위한 discord bot 만들기
기능
- 관리자 사이트 만들기 (react)
- 커뮤니티 인기글로 영상 만들기


## <a id="AI"></a>AI
### 주요 기능
AI 관련 서비스

### 소스
* [ejetool-ai](#ejetool-ai)
* [ejetool-client](#ejetool-client)
  * [ejetool-videoai-client](#ejetool-videoai-client)


