package com.fvote.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TopicUpdateReq {
    @Data
    @Getter
    @Setter
    public static class Topic {
        public Long topicId;
        public String title;
        public String description;
        public Boolean isMultipleChoice;
        public Integer maxChoice;
    }

    @Data
    @Getter
    @Setter
    public static class Choice {
        public Long choiceId;
        public String choiceText;
    }

    public Long userId;
    public Topic topic;
    public Choice[] choices;
}
