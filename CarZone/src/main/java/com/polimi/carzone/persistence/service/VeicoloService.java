package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.strategy.RicercaStrategy;

import java.util.List;

public interface VeicoloService {

    boolean aggiungiVeicolo(AggiuntaVeicoloRequestDTO request);

    DettagliVeicoloResponseDTO recuperaDettagli(long idVeicolo);

    List<VeicoloResponseDTO> findAll();

    Veicolo findByTarga(String targa);

    List<VeicoloResponseDTO> convertiVeicoliInVeicoliResponse(List<Veicolo> veicoliTrovati);

    RicercaStrategy scegliRicerca(String criterio);

    List<Veicolo> findByMarca(String marca);

    List<Veicolo> findByMarcaAndModello(String marca, String modello);

    List<Veicolo> findByAlimentazione(String alimentazione);
}
