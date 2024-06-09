package com.polimi.carzone.exception;

public class AppuntamentoNonAssegnatoException extends RuntimeException {
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public AppuntamentoNonAssegnatoException(String message) {
        super(message);
    }
}
