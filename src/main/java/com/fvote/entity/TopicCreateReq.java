package com.fvote.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TopicCreateReq {
    @Data
    @Getter
    @Setter
    public static class Topic {
        public String title;
        public String description;
        public Boolean isMultipleChoice;
        public Integer maxChoice;
    };

    public Long userId;
    public Topic topic;
    public String[] choices;
}
