package com.ejetool.videoai.event.consumer.processor;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.function.ToIntFunction;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.lib.telegram.util.TelegramUtils;
import com.ejetool.lib.useapi.UseapiApi;
import com.ejetool.lib.useapi.dto.midjourney.JobAttachment;
import com.ejetool.lib.useapi.dto.midjourney.JobButton;
import com.ejetool.lib.useapi.dto.midjourney.JobButtonRequest;
import com.ejetool.lib.useapi.dto.midjourney.JobButtonResponse;
import com.ejetool.lib.useapi.dto.midjourney.JobCancelResponse;
import com.ejetool.lib.useapi.dto.midjourney.JobImagineRequest;
import com.ejetool.lib.useapi.dto.midjourney.JobImagineResponse;
import com.ejetool.lib.useapi.dto.midjourney.JobResponse;
import com.ejetool.lib.useapi.dto.midjourney.JobStatus;
import com.ejetool.lib.useapi.dto.midjourney.JobVerb;
import com.ejetool.videoai.common.content.ContentItemStatus;
import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.domain.entity.ContentItem;
import com.ejetool.videoai.domain.repository.ContentItemRepository;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemImageParam;
import com.ejetool.videoai.event.consumer.exception.ImageGenerateException;
import com.ejetool.videoai.event.consumer.exception.ImageModeratedException;
import com.ejetool.videoai.event.consumer.processor.interfaces.ImageGenerateProcessor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageGenerateProcessorImpl implements ImageGenerateProcessor {

    private final UseapiApi useApiClient;
    private final ContentItemRepository contentItemRepository;

    private Semaphore semaphore = new Semaphore(3);

    static class ImageProcessor{
        private final ContentItem item;

        @Getter
        private LocalDateTime nextTime;

        @Getter
        private int runCount = 0;

        public ImageProcessor(ContentItem item){
            this.item = item;
            this.nextTime = LocalDateTime.now();
        }

        public boolean canStart(){
            return this.nextTime.isBefore(LocalDateTime.now());
        }

        public boolean execute(ToIntFunction<ContentItem> func){
            if(this.canStart()){
                ++runCount;
                try{
                    int waitSec = func.applyAsInt(this.item);
                    if(waitSec < 0){
                        return true;
                    } 
                    this.nextTime = this.nextTime.plusSeconds(waitSec);
                } catch(Exception e){
                    log.error("execute(): Exception.", e);
                }
            }
            return false;
        }
    }

    /**
     * 실행
     */
    @Override 
    public boolean generate(Content content, GenerateItemImageParam param, TelegramChatNotify telegramNotify){
        var contentItems = content.getItems();
        if(contentItems.isEmpty()){
            return false;
        }

        var jobs = contentItems
            .stream()
            .filter(x->!x.isImageFinish(param.isFailRetry()))
            .map(x->new ImageProcessor(x))
            .collect(Collectors.toList());
        
        long lastMessageSentTime = System.currentTimeMillis();
        long messageIntervalMillis = TimeUnit.SECONDS.toMillis(3); // 3 seconds

        while(!jobs.isEmpty()){
            var iterator = jobs.iterator();
            while(iterator.hasNext()){
                ImageProcessor processor = iterator.next();
                try {
                    if(processor.execute(item->this.next(item, param.isFailRetry()))){
                        iterator.remove();
                    }
                } catch(Exception e){
                    iterator.remove();
                }
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMessageSentTime >= messageIntervalMillis) {
                lastMessageSentTime = currentTime;
                this.sendTelegramMessage(telegramNotify, content);
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("generate(): InterruptedException.", e);
            }
        }

        var notCompletedItem = contentItems.stream().filter(x->x.getImageStatus() != ContentItemStatus.COMPLETED).findFirst();
        if(notCompletedItem.isPresent()){
            var item = notCompletedItem.get();
            item.getContent().setImageModerated();
            this.contentItemRepository.save(item);
        } else {
            contentItems.stream().findFirst().ifPresent(item->{
                item.getContent().setItemImageCompleted();
                this.contentItemRepository.save(item);
            });
        }

        // List<Future<?>> futures = new ArrayList<>(jobs.size());
        // for(ContentItem contentItem : jobs) {
        //     futures.add(this.executors.submit(()->this.generateItem(contentItem, param.isFailRetry())));
        // }
        // for(var future : futures){
        //     try {
        //         future.get();
        //     } catch (ExecutionException e) {
        //         log.error("generate().generateItem(): ExecutionException.", e);
        //     } catch (InterruptedException e) {
        //         Thread.currentThread().interrupt();
        //         log.info("generate(): generateItem.", e);
        //     } 
        // }

        // var notCompletedItem = contentItems.stream().filter(x->x.getImageStatus() != ContentItemStatus.COMPLETED).findFirst();
        // if(notCompletedItem.isPresent()){
        //     var item = notCompletedItem.get();
        //     item.getContent().setImageModerated();
        //     this.contentItemRepository.save(item);
        // } else {
        //     contentItems.stream().findFirst().ifPresent(item->{
        //         item.getContent().setItemImageCompleted();
        //         this.contentItemRepository.save(item);
        //     });
        // }
        return true;
    }

    private void sendTelegramMessage(TelegramChatNotify telegramNotify, Content content) {

        var items = content.getItems();
        var completedCount = items.stream().filter(x->x.isImageCompleted()).count();
        
        StringBuilder sbText = new StringBuilder();
        sbText.append(String.format("이미지 생성중... ( %s / %s ) - %s\n", completedCount, items.size(), LocalDateTime.now()));
        
        if(!items.isEmpty()){
            for(int i=0; i<items.size(); i++){
                var item = items.get(i);
                if(StringUtils.hasText(item.getImageUrl())){
                    sbText.append(TelegramUtils.toSafeText(">> [%s][%s] %s. [link](%s)\n", i+1, item.getId(), item.getImageStatus(), item.getImageUrl()));
                } else {
                    sbText.append(TelegramUtils.toSafeText(">> [%s][%s] %s\n", i+1, item.getId(), item.getImageStatus()));
                }
            }

            
        }
        telegramNotify.send(sbText.toString());
    }

    // /**
    //  * 프로세스
    //  * @param contentItem
    //  * @return
    //  * @throws InterruptedException 
    //  */
    // private void generateItem(ContentItem contentItem, boolean failRetry){
    //     log.info("generateItem(): start. contentId={}, contentItemId={}", contentItem.getContent().getId(), contentItem.getId());
    //     int wait = 0;
    //     while(wait > -1){
    //         if(contentItem.isImageFinish(failRetry)){
    //             break;
    //         }

    //         wait = this.next(contentItem, failRetry);
    //         if(wait > 0){
    //             try {
    //                 Thread.sleep(Duration.ofSeconds(wait));
    //             } catch (InterruptedException e) {
    //                 Thread.currentThread().interrupt();
    //                 log.info("generateItem(): InterruptedException.", e);
    //             }
    //         }
            
    //     }
    // }
   
    private int next(ContentItem contentItem, boolean failRetry){
        log.debug("next(): start. contentId={}, contentItemId={}", contentItem.getContent().getId(), contentItem.getId());
        try {
            switch (contentItem.getImageStatus()) {
                case NONE:
                case READY:
                case REQUEST:
                    return this.stepRequest(contentItem);
                case PROCESS:
                    return this.stepProcess(contentItem);
                case MODERATED:
                    log.info("contentItem alreay moderated.");
                    return -1;
                case FAILED:
                    if(failRetry){
                        if(StringUtils.hasText(contentItem.getImageTxId())){
                            contentItem.resetImageReady();
                            this.contentItemRepository.save(contentItem);
                            return this.stepRequest(contentItem);
                        } else {
                            contentItem.setImageProcess(contentItem.getImageTxId());
                            return this.stepRequest(contentItem);
                        }
                    } else {
                        throw new ImageGenerateException("contentItem already fail.");
                    }
                //--------------------------------------------
                //- 
                //--------------------------------------------
                case COMPLETED:
                    return this.stepCompleted(contentItem);
                //--------------------------------------------
                //- 
                //--------------------------------------------
                default:
                    throw new ImageGenerateException(String.format("contentItem image status error. status=%s", contentItem.getImageStatus()));
            }
        } catch(ImageModeratedException e){
            contentItem.setImageModerated();
            this.contentItemRepository.save(contentItem);
            log.warn("next(): error.", e);
        } catch(ImageGenerateException e){
            contentItem.setImageError();
            this.contentItemRepository.save(contentItem);
            log.error("next(): error.", e);
        } catch (InterruptedException e) {
            contentItem.setImageError();
            this.contentItemRepository.save(contentItem);
            Thread.currentThread().interrupt();
            log.info("next(): InterruptedException.", e);
        } catch(Exception e){
            contentItem.setImageError();
            this.contentItemRepository.save(contentItem);
            log.error("next(): Exception.", e);
        } 
        return -1;
    }

    /**
     * midjourney prompt
     * 미드저니 이미지 생성 요청
     * @param itemId
     * @param subject
     * @param text
     * @return
     * @throws InterruptedException
     */
    private JobImagineResponse midjourneyImagine(long itemId, String subject, String text, String prompt){
        var finalPrompt = new StringBuilder();
        finalPrompt.append("Anime luminous. ");

        if(StringUtils.hasText(prompt)){
            finalPrompt.append(prompt);
        } else {
            // ex: You can achieve your dreams if you don't give up.|Yuna Kim|Female|Interview|Sports Star
            var texts = text.split("\\|");

            finalPrompt.append(" ").append(subject).append(".");
            if(texts.length > 4){
                // job
                finalPrompt.append(" ").append(texts[4]).append("."); 
            }
    
            finalPrompt.append(" ").append(texts[0]); // quote
            if(!texts[0].endsWith(".")){
                finalPrompt.append(".");
            }
        }
        finalPrompt.append(" --ar 3:4 --q .25");

        var request = JobImagineRequest.builder()
            .prompt(finalPrompt.toString())
            .replyRef(String.format("contentItem:", itemId))
            .build();
            
        JobImagineResponse response = this.useApiClient.midjourney().jobImagine(request);
        return response;
    }

    /**
     * midjourney button
     * prompt로 생성된 이미지에서 버튼 클릭
     * @param itemId
     * @param jobId
     * @param button
     * @return
     * @throws InterruptedException
     */
    private JobButtonResponse midjourneyButton(long itemId, String jobId, String button){
        var request = JobButtonRequest.builder()
            .jobId(jobId)
            .button(button)
            .replyRef(String.format("contentItem:", itemId))
            .build();
        
        JobButtonResponse response = this.useApiClient.midjourney().jobButton(request);
        return response;
    }

    /**
     * job cancel
     * @param jobId
     * @return
     */
    private JobCancelResponse midjourneyCancel(String jobId){
        JobCancelResponse response = this.useApiClient.midjourney().jobCancel(jobId);
        return response;
    }

    /**
     * 이미지 생성 요청
     * @param contentItem
     * @return
     * @throws InterruptedException 
     */
    private int stepRequest(ContentItem contentItem) throws InterruptedException{
        log.info("stepRequest(): start. contentId={}, contentItemId={}", contentItem.getContent().getId(), contentItem.getId());

        var jobId = contentItem.getImageTxId();
        if(!StringUtils.hasText(jobId)){

            boolean acquire = this.semaphore.tryAcquire();
            if(!acquire){
                return 1;
            }

            try {
                var imagineRes = this.midjourneyImagine(contentItem.getId(), contentItem.getContent().getSubjectEng(), contentItem.getTextEng(), contentItem.getImagePrompt());
                if(!imagineRes.isSucceed()){
                    switch (HttpStatus.valueOf(imagineRes.getCode())) { // NOSONAR
                        case TOO_MANY_REQUESTS:
                            return 30;
                        default:
                            log.error("midjourney.jobImagine(): error. status={}, error={}, desc={}", imagineRes.getStatus(), imagineRes.getError(), imagineRes.getErrorDetails());
                            throw new ImageGenerateException(String.format("useApiClient.midjourney.jobImagine():error. code=%s",imagineRes.getCode()));
                    }
                }

                jobId = imagineRes.getJobId();
                contentItem.setImageProcess(jobId);
                this.contentItemRepository.save(contentItem);
                return 30;
            } finally {
                this.semaphore.release();
            }
        } else {
            contentItem.setImageProcess(jobId);
            this.contentItemRepository.save(contentItem);
        }
        return 0;
    }

    /**
     * 이미지 생성중
     * @param contentItem
     * @return
     * @throws InterruptedException 
     */
    @SuppressWarnings({"squid:S3776", "squid:S6541"})
    private int stepProcess(ContentItem contentItem) throws InterruptedException {
        log.info("stepProcess(): start. contentId={}, contentItemId={}", contentItem.getContent().getId(), contentItem.getId());

        var jobId = contentItem.getImageTxId();
        if(!StringUtils.hasText(jobId)){
            contentItem.resetImageReady();
            this.contentItemRepository.save(contentItem);
            return 0;
        }

        JobResponse jobResponse = this.useApiClient.midjourney().getJob(jobId);
        if(!jobResponse.isSucceed()){
            switch (HttpStatus.valueOf(jobResponse.getCode())) { // NOSONAR
                case UNPROCESSABLE_ENTITY:
                    // job status가 moderated 상태가 되면 이후에 422(UNPROCESSABLE_ENTITY) 상태 코드로 변환됨
                    // 이 경우 이미지가 본인만 볼 수 있는 글로 변경되어서 userapi가 접근이 안되는것 같음
                    log.warn("midjourney.jobImagine(): moderated. status={}, error={}, desc={}", jobResponse.getStatus(), jobResponse.getError(), jobResponse.getErrorDetails());
                    throw new ImageModeratedException(String.format("useApiClient.midjourney.getJob(%s):error. code=%s", jobId, jobResponse.getCode()));
                case TOO_MANY_REQUESTS:
                    return 30;
                default:
                    log.error("midjourney.jobImagine(): error. status={}, error={}, desc={}", jobResponse.getStatus(), jobResponse.getError(), jobResponse.getErrorDetails());
                    throw new ImageGenerateException(String.format("useApiClient.midjourney.getJob(%s):error. code=%s", jobId, jobResponse.getCode()));
            }
        }
        
        if(!jobResponse.getStatus().equals(JobStatus.COMPLETED)){ // NOSONAR
            switch (jobResponse.getStatus()) {
                case JobStatus.CREATED:
                case JobStatus.STARTED:
                case JobStatus.PROGRESS:
                    // discord에서 응답이 끝났는데 useapi가 중계 실패하는 경우가 있다.
                    // 트릭으로 updated 시간이 2분 경과하면 작업을 다시 요청.
                    Instant updated = Instant.parse(jobResponse.getUpdated());
                    if(Duration.between(updated, Instant.now()).toSeconds() > 120){
                        // 작업을 부모 단계로 복구
                        if(StringUtils.hasText(jobResponse.getParentJobId())) {
                            contentItem.setImageProcess(jobResponse.getParentJobId());
                        } else {
                            contentItem.resetImageReady();
                        }
                        var jobCancelResponse = this.midjourneyCancel(jobId);
                        if(!jobCancelResponse.isSucceed()){
                            log.error("midjourneyCancel({}): failed. status={}", jobId, jobCancelResponse.getStatus());
                        }
                        this.contentItemRepository.save(contentItem);
                        return 0;
                    }
                    return 10;
                case JobStatus.MODERATED:
                    log.error("midjourney job moderated. jobId={}", jobId);
                    throw new ImageModeratedException(String.format("midjourney job moderated. jobId=%s", jobId));
                case JobStatus.FAILED:
                    log.error("midjourney job failed. jobId={}", jobId);
                    throw new ImageGenerateException(String.format("midjourney job failed. jobId=%s", jobId));
                case JobStatus.CANCELLED:
                    log.error("midjourney job cancelled. jobId={}", jobId);
                    // 처음부터 작업을 다시 진행
                    // - 부모 단계가 있는 경우 돌아가려 했는데 부모 job에 button child가 남아 있어서 중복 요청이 안된다
                    contentItem.resetImageReady();
                    this.contentItemRepository.save(contentItem);
                    return 0;
                default:
                    log.error("midjourney job error. status={}", jobId, jobResponse.getStatus());
                    throw new ImageGenerateException(String.format("midjourney job error. status=%s", jobId, jobResponse.getStatus()));
            }
        } 
        
        if(JobVerb.IMAGINE.equals(jobResponse.getVerb())){ // NOSONAR
            // 아직 IMAGINE 단계일 경우 여러개의 이미지중 1번째 이미지로 바로 확정
            // 추후 이 부분을 사용자에게 여러 이미지 중 선택하는 step을 넣어주자
            var selectButton = JobButton.U2;

            // button을 중복 요청할 수 없고 요청 정보는 children 안에 있음
            // 이전 요청 정보르 job 갱신
            if(jobResponse.getChildren() != null){
                var alreadyChild = jobResponse.getChildren().stream()
                    .filter(x->selectButton.equals(x.getButton()))
                    .findFirst();
                if(alreadyChild.isPresent()){
                    contentItem.setImageProcess(alreadyChild.get().getJobId());
                    this.contentItemRepository.save(contentItem);
                    return 0;
                }
            }

            boolean acquire = this.semaphore.tryAcquire();
            if(!acquire){
                return 1;
            }

            try {
                var jobButtonResponse = this.midjourneyButton(contentItem.getId(), jobId, selectButton);
                if(!jobButtonResponse.isSucceed()){
                    switch (HttpStatus.valueOf(jobButtonResponse.getCode())) { // NOSONAR
                        case CONFLICT:
                            return 30;
                        case TOO_MANY_REQUESTS:
                            return 30;
                        default:
                            throw new ImageGenerateException(String.format("useApiClient.midjourney.jobButton():error. code=%s", jobButtonResponse.getCode()));
                    }
                }
                jobId =  jobButtonResponse.getJobId();
                contentItem.setImageProcess(jobId);
                return 30;
            } finally {
                this.semaphore.release();
            }
        } else if(JobVerb.BUTTON.equals(jobResponse.getVerb())){ // NOSONAR
            var url = jobResponse.getAttachments().stream()
                .map(JobAttachment::getUrl)
                .filter(xdd->StringUtils.hasText(xdd))
                .findFirst();
            if(url.isPresent()){
                contentItem.setImageCompleted(url.get());
                this.contentItemRepository.save(contentItem);
                return 0;
            } else {
                throw new ImageGenerateException(String.format("useApiClient.midjourney.getJob(%s):attachment empty.", jobId));
            }
        } else {
            throw new ImageGenerateException(String.format("useApiClient.midjourney.getJob(%s):verb error. code=%s", jobId, jobResponse.getVerb()));
        }
    }

    /**
     * 이미지 생성 완료
     * @param contentItem
     * @return
     */
    private int stepCompleted(ContentItem contentItem) {
        log.info("stepCompleted(): start. contentId={}, contentItemId={}", contentItem.getContent().getId(), contentItem.getId());

        if(!contentItem.isImageUrl()){
            if(contentItem.isImageTxId()){
                contentItem.setImageProcess(contentItem.getImageTxId());
            } else {
                contentItem.resetImageReady();
            }
            this.contentItemRepository.save(contentItem);
        }
        return -1;
    }

    
}
