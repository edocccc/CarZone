package com.polimi.carzone.exception;

public class VeicoliNonDisponibiliException extends RuntimeException {

    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public VeicoliNonDisponibiliException(String message) {
        super(message);
    }
}
