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

@RequiredArgsConstructor
@Component
public class RicercaManager {

    private final VeicoloService veicoloService;

    public RicercaStrategy scegliRicerca(String criterio) {
        if(criterio == null || criterio.isEmpty() || criterio.isBlank()) {
            throw new CriterioNonValidoException("Inserisci un criterio di ricerca valido");
        }
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

    public List<Veicolo> executeRicerca(RicercaStrategy ricercaStrategy, RicercaRequestDTO ricercaRequestDTO) {
        return ricercaStrategy.ricerca(ricercaRequestDTO);
    }

}
