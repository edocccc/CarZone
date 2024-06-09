package com.polimi.carzone.exception;

public class RuoloNonValidoException extends RuntimeException{

        // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
        public RuoloNonValidoException(String message) {
            super(message);
        }
}
