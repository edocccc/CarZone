package com.polimi.carzone.strategy;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;

import java.util.List;

// interfaccia che definisce il metodo di ricerca a seconda del tipo di ricerca selezionato
public interface RicercaStrategy {
    // metodo che verrà implementato dalle classi che rappresentano le strategie concrete
    // la ricerca avviene a seconda del tipo di ricerca selezionato precedentemente e ricevuto come parametro in input
    List<Veicolo> ricerca(RicercaRequestDTO request);
}
