package com.polimi.carzone.state;

import com.polimi.carzone.model.Veicolo;

public abstract class State {

    Veicolo veicolo;

    protected State(Veicolo veicolo){
        this.veicolo = veicolo;
    }

    public abstract void cambiaStato();


}
