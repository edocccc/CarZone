package com.polimi.carzone.strategy.context;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.exception.CriterioNonValidoException;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import com.polimi.carzone.strategy.implementation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

//annotazione che genera un costruttore con un parametro per ciascun campo
@RequiredArgsConstructor
//annotazione che permette di definire un bean
@Component
public class RicercaManager {
    //la classe permette di gestire la ricerca di veicoli in base a diversi criteri
    //viene utilizzata per scegliere la strategia di ricerca in base al criterio inserito dall'utente
    //contiene inoltre il metodo per eseguire la ricerca vera e propria


    //dichiarazione del service per la gestione dei veicoli
    private final VeicoloService veicoloService;

    //metodo che permette di scegliere la strategia di ricerca in base al criterio inserito dall'utente
    public RicercaStrategy scegliRicerca(String criterio) {
        //controllo del criterio inserito
        //se il criterio è nullo, vuoto o composto solo da spazi viene lanciata un'eccezione
        if(criterio == null || criterio.isEmpty() || criterio.isBlank()) {
            throw new CriterioNonValidoException("Inserisci un criterio di ricerca valido");
        }
        //se il criterio è diverso da quelli previsti viene lanciata un'eccezione
        //se invece il criterio è valido viene restituita la strategia di ricerca corrispondente
        return switch (criterio) {
            case "targa" -> new RicercaTarga(veicoloService);
            case "marca" -> new RicercaMarca(veicoloService);
            case "marcamodello" -> new RicercaMarcaAndModello(veicoloService);
            case "alimentazione" -> new RicercaAlimentazione(veicoloService);
            case "annoProduzione" -> new RicercaAnnoProduzione(veicoloService);
            case "prezzo" -> new RicercaPrezzo(veicoloService);
            case "potenza" -> new RicercaPotenza(veicoloService);
            case "chilometraggio" -> new RicercaChilometraggio(veicoloService);
            default -> throw new CriterioNonValidoException("Criterio di ricerca non valido");
        };
    }

    //metodo che permette di eseguire la ricerca vera e propria una volta scelta la strategia e passati i parametri con la richiesta
    public List<Veicolo> executeRicerca(RicercaStrategy ricercaStrategy, RicercaRequestDTO ricercaRequestDTO) {
        //viene chiamato il metodo ricerca della strategia scelta passando i parametri della richiesta
        return ricercaStrategy.ricerca(ricercaRequestDTO);
    }

}
