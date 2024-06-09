package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

//annotazione per generare un costruttore con un parametro per ogni campo
@RequiredArgsConstructor
public class RicercaMarca implements RicercaStrategy {
    //la classe implementa l'interfaccia RicercaStrategy e implementa il metodo ricerca
    //fornisce quindi la logica per la ricerca di veicoli in base alla marca

    //dichiarazione del service per la gestione dei veicoli
    private final VeicoloService veicoloService;

    //metodo per la ricerca di veicoli in base alla marca e alla richiesta ricevuta
    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        //dichiarazione di una lista di veicoli
        List<Veicolo> veicoli;
        //estrazione della marca cercata dalla richiesta
        String marcaCercata = request.getMarca();
        //invocazione del metodo findByMarca del service per ottenere i veicoli con la marca cercata
        veicoli = veicoloService.findByMarca(marcaCercata);
        //restituzione della lista di veicoli
        return veicoli;
    }
}
