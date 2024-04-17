package com.polimi.carzone.state.implementation;

import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.state.State;

public class Venduto extends State {

    public Venduto(Veicolo veicolo) {
        super(veicolo);
    }

    @Override
    public void cambiaStato() {

    }
}
