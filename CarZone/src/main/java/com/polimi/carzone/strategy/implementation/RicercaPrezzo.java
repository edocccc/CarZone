package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

//annotazione per generare un costruttore con un parametro per ogni campo
@RequiredArgsConstructor
public class RicercaPrezzo implements RicercaStrategy {
    //la classe implementa l'interfaccia RicercaStrategy e implementa il metodo ricerca
    //fornisce quindi la logica per la ricerca di veicoli in base al prezzo

    //dichiarazione del service per la gestione dei veicoli
    private final VeicoloService veicoloService;

    //metodo per la ricerca di veicoli in base al prezzo e alla richiesta ricevuta
    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        //dichiarazione di una lista di veicoli
        List<Veicolo> veicoli;
        //estrazione del prezzo minimo e massimo cercato dalla richiesta
        Double prezzoMinimoCercato = request.getPrezzoMinimo();
        Double prezzoMassimoCercato = request.getPrezzoMassimo();
        //invocazione del metodo findByPrezzo del service per ottenere i veicoli con il prezzo nel range cercato
        veicoli = veicoloService.findByPrezzo(prezzoMinimoCercato, prezzoMassimoCercato);
        //restituzione della lista di veicoli
        return veicoli;
    }
}
