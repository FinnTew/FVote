package com.fvote.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomException extends RuntimeException {

    private int code;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

}
