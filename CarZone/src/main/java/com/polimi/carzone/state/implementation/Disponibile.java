package com.polimi.carzone.state.implementation;

import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.state.State;
import org.springframework.stereotype.Component;

@Component
public class Disponibile implements State<Veicolo> {

    @Override
    public void cambiaStato(Veicolo context) {

    }
}
