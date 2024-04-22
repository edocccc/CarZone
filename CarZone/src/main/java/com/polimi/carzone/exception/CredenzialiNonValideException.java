package com.polimi.carzone.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CredenzialiNonValideException extends RuntimeException {

    Map<String,String> errori;

    public CredenzialiNonValideException(Map<String,String> errori) {
        this.errori = errori;
    }

}
