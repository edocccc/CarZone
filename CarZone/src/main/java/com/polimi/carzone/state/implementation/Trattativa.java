package com.polimi.carzone.state.implementation;

import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.state.State;

public class Trattativa extends State {

    public Trattativa(Veicolo veicolo) {
        super(veicolo);
    }

    @Override
    public void onDisponibile() {
        veicolo.cambiaStato(new Disponibile(veicolo));
    }

    @Override
    public void onVenduto() {
        veicolo.cambiaStato(new Venduto(veicolo));
    }

    @Override
    public void onTrattativa() {
        veicolo.cambiaStato(new Trattativa(veicolo));
    }
}
