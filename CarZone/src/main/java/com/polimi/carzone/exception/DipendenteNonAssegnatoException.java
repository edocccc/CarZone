package com.polimi.carzone.exception;

public class DipendenteNonAssegnatoException extends RuntimeException{
    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public DipendenteNonAssegnatoException(String message) {
        super(message);
    }
}
