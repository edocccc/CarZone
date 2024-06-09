package com.polimi.carzone.exception;

public class TokenNonValidoException extends RuntimeException{
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public TokenNonValidoException(String message) {
        super(message);
    }
}
