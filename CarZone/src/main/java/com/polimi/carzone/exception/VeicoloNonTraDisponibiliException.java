package com.polimi.carzone.exception;

public class VeicoloNonTraDisponibiliException extends RuntimeException{
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public VeicoloNonTraDisponibiliException(String message) {
        super(message);
    }
}
