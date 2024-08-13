package com.ejetool.videoai.service.dto.prompt;

import java.util.List;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromptContentResult{
    @JsonAlias("s")
    @JsonProperty("subject")
    private String text;

    @JsonAlias("se")
    @JsonProperty("subject_eng")
    private String textEng;

    @JsonAlias("is")
    @JsonProperty("items")
    private List<Item> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item{

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Text {
            @JsonAlias("q")
            @JsonProperty("quote")
            private String quote;

            @JsonAlias("n")
            @JsonProperty("name")
            private String name;

            @JsonAlias("g")
            @JsonProperty("gender")
            private String gender;

            @JsonAlias("r")
            @JsonProperty("ref")
            private String ref;

            @JsonAlias("j")
            @JsonProperty("job")
            private String job;

            public boolean isValid() {
                return StringUtils.hasText(this.quote)
                    && StringUtils.hasText(this.name)
                    && StringUtils.hasText(this.gender)
                    && StringUtils.hasText(this.ref);
            }

            public String toJoinString(){
                return String.join("|", this.quote, this.name, this.gender, this.ref, this.job);
            }
        }

        @JsonAlias("t")
        @JsonProperty("text")
        private Text text;

        @JsonAlias("te")
        @JsonProperty("text_eng")
        private Text textEn;

        @JsonAlias("tk")
        @JsonProperty("text_keyword")
        private String textKeyword;

        @JsonAlias("tke")
        @JsonProperty("text_keyword_eng")
        private String textEngKeyword;

        @JsonAlias("ip")
        @JsonProperty("image_prompt")
        private String imagePrompt;
    }
}