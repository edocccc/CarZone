package com.polimi.carzone.exception;

public class CriterioNonValidoException extends RuntimeException{

    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public CriterioNonValidoException(String message) {
        super(message);
    }
}
