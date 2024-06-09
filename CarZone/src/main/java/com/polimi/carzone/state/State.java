package com.polimi.carzone.state;

import com.polimi.carzone.model.Veicolo;

public abstract class State {
    // classe astratta per gestire i diversi stati di un veicolo

    // riferimento al veicolo a cui si riferisce lo stato
    protected Veicolo veicolo;

    // costruttore con parametro veicolo
    protected State(Veicolo veicolo) {
        this.veicolo = veicolo;
    }

    // metodo astratto per gestire lo stato disponibile
    public abstract void onDisponibile();

    // metodo astratto per gestire lo stato venduto
    public abstract void onVenduto();

    // metodo astratto per gestire lo stato trattativa
    public abstract void onTrattativa();

}
