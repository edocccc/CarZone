package com.polimi.carzone.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CredenzialiNonValideException extends RuntimeException {

    // mappa contenente gli errori relativi ai campi non validi
    Map<String,String> errori;

    // costruttore della classe che definisce gli errori da visualizzare per l'eccezione
    public CredenzialiNonValideException(Map<String,String> errori) {
        this.errori = errori;
    }

}
