package com.fvote.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserRegisterReq {
    public String username;

    public String password;

    public String email;
}
