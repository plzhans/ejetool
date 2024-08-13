package com.ejetool.videoai.event.consumer.processor;

import java.io.IOException;
import java.net.URLConnection;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.videoai.common.content.ContentItemStatus;
import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.domain.entity.ContentItem;
import com.ejetool.videoai.domain.repository.ContentItemRepository;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemVoiceParam;
import com.ejetool.videoai.event.consumer.exception.VoiceGenerateException;
import com.ejetool.videoai.event.consumer.helper.GoogleDriveHelper;
import com.ejetool.videoai.event.consumer.processor.interfaces.VoiceGenerateProcessor;
import com.ejetool.videoai.event.consumer.setting.GoogleDriveSettings;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoiceGenerateProcessorImpl implements VoiceGenerateProcessor{
    
    private final GoogleDriveSettings googleDriveSettings;
    private final ContentItemRepository contentItemRepository;
    private final TextToSpeechClient textToSpeechClient;
    private final Drive googleDrive;

    private ExecutorService executors = Executors.newFixedThreadPool(1);

    private boolean isFinish(ContentItem contentItem, boolean failRetry) {
        ContentItemStatus status = contentItem.getVoiceStatus();
        if(contentItem.isVoiceUrl() && contentItem.isVoiceTxId()){
            return status == ContentItemStatus.COMPLETED || (!failRetry && status == ContentItemStatus.FAILED);
        }
        return false;
    }

    @Override
    public boolean generate(Content content, GenerateItemVoiceParam param, TelegramChatNotify telegramNotify){
        var contentItems = content.getItems();
        if(contentItems.isEmpty()){
            return false;
        }

        var jobs = contentItems
            .stream()
            .filter(x->param.isForce() || !this.isFinish(x, param.isFailRetry())).toList();
        if(jobs.isEmpty()){
            return true;
        }

        List<Future<?>> futures = new ArrayList<>(jobs.size());
        for(ContentItem contentItem : jobs) {
            futures.add(this.executors.submit(()->this.generateItem(contentItem, param.isFailRetry())));
        }
        for(var future : futures){
            try {
                future.get();
            } catch (ExecutionException e) {
                log.error("generate().generateItem(): ExecutionException.", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("generate(): generateItem.", e);
            } 
        }
        return true;
    }

    /**
     * 프로세스
     * @param contentItem
     * @return
     * @throws InterruptedException 
     */
    private void generateItem(ContentItem contentItem, boolean failRetry){
        log.info("generateItem(): start. contentId={}, contentItemId={}", contentItem.getContent().getId(), contentItem.getId());
        int wait = 0;
        while(wait > -1){
            if(isFinish(contentItem, failRetry)){
                break;
            }

            wait = this.next(contentItem, failRetry);
            if(wait > 0){
                try {
                    Thread.sleep(Duration.ofSeconds(wait).toMillis());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("generateItem(): InterruptedException.", e);
                }
            }
        }
    }

    private int next(ContentItem contentItem, boolean failRetry){
        log.debug("next({},{}): start.", contentItem.getContent().getId(), contentItem.getId());
        try {
            switch (contentItem.getVoiceStatus()) {
                case NONE:
                case READY:
                case REQUEST:
                case PROCESS:
                    return this.stepProcess(contentItem);
                case FAILED:
                    if(failRetry){
                        return this.stepProcess(contentItem);
                    } else {
                        throw new VoiceGenerateException(String.format("next(%s,%s): fail.", contentItem.getContent().getId(), contentItem.getId()));
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
                    throw new VoiceGenerateException(String.format("next(%s,%s): status error. status=%s", contentItem.getContent().getId(), contentItem.getId(), contentItem.getVoiceStatus()));
            }
        } catch(VoiceGenerateException e){
            contentItem.setVoiceError();
            this.contentItemRepository.save(contentItem);
            log.error(String.format("next(%s,%s): error.", contentItem.getContent().getId(), contentItem.getId()), e);
        } 
        catch(Exception e){
            contentItem.setVoiceError();
            this.contentItemRepository.save(contentItem);
            log.error(String.format("next(%s,%s): Exception.", contentItem.getContent().getId(), contentItem.getId()), e);
        } 
        return -1;
    }

    private int stepProcess(ContentItem contentItem) {

        String filePath = String.format("%s/content-%s", googleDriveSettings.getPath(), contentItem.getContent().getId());
        String fileName = String.format("content_%s_%s_voice.mp3", contentItem.getContent().getId(), contentItem.getId());
        String mimeType = URLConnection.guessContentTypeFromName(fileName);

        try {
            File folder = GoogleDriveHelper.getFolderByPathOrCreate(this.googleDrive, googleDriveSettings.getRootId(), filePath);
            var googleFileOptional = GoogleDriveHelper.getFirstFileByName(googleDrive, folder.getId(), fileName, mimeType);
            if(!googleFileOptional.isPresent()){
                byte[] mp3Bytes = this.textToSound(contentItem.getText());
                File uploadFile = GoogleDriveHelper.uploadFile(this.googleDrive, folder.getId(), fileName, mimeType, mp3Bytes);
                
                GoogleDriveHelper.setWebShareAnyoneReader(this.googleDrive, uploadFile.getId());
                
                contentItem.setVoiceCompleted(uploadFile.getId(), uploadFile.getWebContentLink());
            } else {
                var googleFile = googleFileOptional.get();
                contentItem.setVoiceCompleted(googleFile.getId(), googleFile.getWebContentLink());
            }
            this.contentItemRepository.save(contentItem);
        } catch (IOException e) {
            throw new VoiceGenerateException(String.format("stepProcess(%s,%s): IOException", contentItem.getContent().getId(), contentItem.getId()));
        }
        return 0;
    }

    private int stepCompleted(ContentItem contentItem) {
        log.info("stepCompleted(): start. contentId={}, contentItemId={}", contentItem.getContent().getId(), contentItem.getId());

        if(!contentItem.isVoiceUrl() || !contentItem.isVoiceTxId()){
            contentItem.resetVoiceReady();
            this.contentItemRepository.save(contentItem);
        }
        return 0;
    }

    private String getGoogleSpeechVoiceName(String language, String gender){
        if(gender != null && gender.startsWith("여")){
            return language + "-Neural2-B";
        }
        return language+"-Neural2-C";
    }

    /**
     * 텍스트를 구글 text to speech api를 이용해서 음성 데이터로 변환
     * @param text
     * @return
     */
    private byte[] textToSound(String text){
        var split = text.split("\\|");

        var finalText = new StringBuilder();
        finalText.append(split[0]);
        if(!split[0].endsWith(".")){
            finalText.append('.');
        }
        if(split.length > 1){
            finalText.append(' ');
            finalText.append(split[1]);
        }
        
        SynthesisInput input = SynthesisInput.newBuilder()
            .setText(finalText.toString())
            .build();

        var locale = Locale.KOREA;
        var languageCode = String.format("%s-%s", locale.getLanguage(), locale.getCountry());
        var voiceName = this.getGoogleSpeechVoiceName(languageCode, split.length > 2 ? split[2] : null);
        
        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode(languageCode)
                .setName(voiceName)
                .build();

        AudioConfig audioConfig = AudioConfig.newBuilder()
            .setAudioEncoding(AudioEncoding.MP3)
            .build();
        try{
            SynthesizeSpeechResponse response = this.textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();
            return audioContents.toByteArray();
        } catch(Exception e){
            throw new VoiceGenerateException("textToSpeechClient.synthesizeSpeech(): Exception.", e);
        }
    }
}
