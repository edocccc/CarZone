package com.polimi.carzone.exception;

public class AppuntamentoPassatoException extends RuntimeException {
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public AppuntamentoPassatoException(String message) {
        super(message);
    }
}
