package com.polimi.carzone.exception;

public class AppuntamentoNonTrovatoException extends RuntimeException{

    // costruttore della classe che definisce il messaggio da visualizzare per l'eccezione
    public AppuntamentoNonTrovatoException(String message) {
        super(message);
    }
}
