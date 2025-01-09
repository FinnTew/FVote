package com.fvote.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserForgetPwdReq {
    public String username;

    public String oldPassword;

    public String newPassword;
}
