package com.fvote.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class TopicCreateResp {
    public Topics topic;

    public List<Choices> choices;
}
