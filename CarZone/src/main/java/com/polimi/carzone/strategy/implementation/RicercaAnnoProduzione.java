package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

//annotazione per generare un costruttore con un parametro per ogni campo
@RequiredArgsConstructor
public class RicercaAnnoProduzione implements RicercaStrategy {
    //la classe implementa l'interfaccia RicercaStrategy e implementa il metodo ricerca
    //fornisce quindi la logica per la ricerca di veicoli in base all'anno di produzione

    //dichiarazione del service per la gestione dei veicoli
    private final VeicoloService veicoloService;

    //metodo per la ricerca di veicoli in base all'anno di produzione e alla richiesta ricevuta
    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        //dichiarazione di una lista di veicoli
        List<Veicolo> veicoli;
        //estrazione dell'anno di produzione minimo e massimo cercato dalla richiesta
        Integer annoProduzioneMinimoCercato = request.getAnnoProduzioneMinimo();
        Integer annoProduzioneMassimoCercato = request.getAnnoProduzioneMassimo();
        //invocazione del metodo findByAnnoProduzione del service per ottenere i veicoli con l'anno di produzione nel range cercato
        veicoli = veicoloService.findByAnnoProduzione(annoProduzioneMinimoCercato, annoProduzioneMassimoCercato);
        //restituzione della lista di veicoli
        return veicoli;
    }
}
