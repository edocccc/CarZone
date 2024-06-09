package com.polimi.carzone.exception;

public class AlimentazioneNonValidaException extends RuntimeException {

    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public AlimentazioneNonValidaException(String message) {
        super(message);
    }
}
