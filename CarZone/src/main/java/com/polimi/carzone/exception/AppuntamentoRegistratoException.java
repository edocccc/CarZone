package com.polimi.carzone.exception;

public class AppuntamentoRegistratoException extends RuntimeException{
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public AppuntamentoRegistratoException(String message) {
        super(message);
    }
}
