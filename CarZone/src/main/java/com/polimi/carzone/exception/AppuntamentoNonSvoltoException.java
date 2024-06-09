package com.polimi.carzone.exception;

public class AppuntamentoNonSvoltoException extends RuntimeException{
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public AppuntamentoNonSvoltoException(String message) {
        super(message);
    }
}
