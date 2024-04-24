package com.polimi.carzone.state;

import com.polimi.carzone.model.Veicolo;

public abstract class State {
    protected Veicolo veicolo;

    protected State(Veicolo veicolo) {
        this.veicolo = veicolo;
    }

    public abstract void onDisponibile();

    public abstract void onVenduto();

    public abstract void onTrattativa();

}
