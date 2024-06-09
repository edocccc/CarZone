package com.polimi.carzone.exception;

public class UtenteNonTrovatoException extends RuntimeException{

    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public UtenteNonTrovatoException(String message) {
        super(message);
    }
}
