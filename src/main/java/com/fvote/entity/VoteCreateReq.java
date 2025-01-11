package com.fvote.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VoteCreateReq {
    public Long topicId;

    public Long[] choiceIds;
}
