package com.polimi.carzone.state;

import com.polimi.carzone.model.Veicolo;

public interface State<T> {

    void cambiaStato(T context);

}
