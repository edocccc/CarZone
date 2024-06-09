package com.polimi.carzone.state.implementation;

import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.state.State;

public class Venduto extends State {
    //classe che estende State e rappresenta lo stato "Venduto" di un veicolo

    //costruttore che richiama il costruttore della superclasse con il veicolo
    public Venduto(Veicolo veicolo) {
        super(veicolo);
    }

    //metodo per cambiare lo stato del veicolo a "Disponibile"
    @Override
    public void onDisponibile() {
        //cambia lo stato del veicolo a "Disponibile"
        veicolo.cambiaStato(new Disponibile(veicolo));
    }

    //metodo per cambiare lo stato del veicolo a "Venduto"
    @Override
    public void onVenduto() {
        //cambia lo stato del veicolo a "Venduto"
        veicolo.cambiaStato(new Venduto(veicolo));
    }

    //metodo per cambiare lo stato del veicolo a "Trattativa"
    @Override
    public void onTrattativa() {
        //cambia lo stato del veicolo a "Trattativa"
        veicolo.cambiaStato(new Trattativa(veicolo));
    }
}
