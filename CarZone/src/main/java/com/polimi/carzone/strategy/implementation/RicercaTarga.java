package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

//annotazione per generare un costruttore con un parametro per ogni campo
@RequiredArgsConstructor
public class RicercaTarga implements RicercaStrategy {
    //la classe implementa l'interfaccia RicercaStrategy e implementa il metodo ricerca
    //fornisce quindi la logica per la ricerca di un veicolo in base alla targa

    //dichiarazione del service per la gestione dei veicoli
    private final VeicoloService veicoloService;

    //metodo per la ricerca di un veicolo in base alla targa e alla richiesta ricevuta
    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        //dichiarazione di una lista di veicoli
        List<Veicolo> veicoli;
        //estrazione della targa cercata dalla richiesta
        String targaCercata = request.getTarga();
        //invocazione del metodo findByTarga del service per ottenere il veicolo con la targa cercata
        Veicolo veicolo = veicoloService.findByTarga(targaCercata);
        //creazione di una lista con il veicolo trovato
        veicoli = List.of(veicolo);
        //restituzione della lista di veicoli
        return veicoli;
    }
}
