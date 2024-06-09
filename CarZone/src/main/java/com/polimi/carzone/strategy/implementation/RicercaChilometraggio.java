package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

//annotazione per generare un costruttore con un parametro per ogni campo
@RequiredArgsConstructor
public class RicercaChilometraggio implements RicercaStrategy {
    //la classe implementa l'interfaccia RicercaStrategy e implementa il metodo ricerca
    //fornisce quindi la logica per la ricerca di veicoli in base al chilometraggio

    //dichiarazione del service per la gestione dei veicoli
    private final VeicoloService veicoloService;

    //metodo per la ricerca di veicoli in base al chilometraggio e alla richiesta ricevuta
    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        //dichiarazione di una lista di veicoli
        List<Veicolo> veicoli;
        //estrazione del chilometraggio minimo e massimo cercato dalla richiesta
        Integer chilometraggioMinimoCercato = request.getChilometraggioMinimo();
        Integer chilometraggioMassimoCercato = request.getChilometraggioMassimo();
        //invocazione del metodo findByChilometraggio del service per ottenere i veicoli con il chilometraggio nel range cercato
        veicoli = veicoloService.findByChilometraggio(chilometraggioMinimoCercato, chilometraggioMassimoCercato);
        //restituzione della lista di veicoli
        return veicoli;
    }
}
