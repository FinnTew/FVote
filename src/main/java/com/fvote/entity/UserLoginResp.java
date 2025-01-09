package com.fvote.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserLoginResp {
    public Long userId;

    public String username;

    public String token;
}
