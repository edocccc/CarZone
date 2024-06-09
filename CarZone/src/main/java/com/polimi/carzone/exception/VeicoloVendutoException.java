package com.polimi.carzone.exception;

public class VeicoloVendutoException extends RuntimeException{
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public VeicoloVendutoException(String message) {
        super(message);
    }
}
