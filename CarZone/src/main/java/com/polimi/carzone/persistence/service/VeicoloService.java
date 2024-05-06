package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.strategy.RicercaStrategy;

import java.util.List;

public interface VeicoloService {

    boolean aggiungiVeicolo(AggiuntaVeicoloRequestDTO request);

    DettagliVeicoloResponseDTO recuperaDettagli(long idVeicolo);

    List<VeicoloResponseDTO> findAll();

    Veicolo findByTarga(String targa);

    Veicolo findById(long id);

    List<VeicoloResponseDTO> convertiVeicoliInVeicoliResponse(List<Veicolo> veicoliTrovati);

    RicercaStrategy scegliRicerca(String criterio);

    List<Veicolo> findByMarca(String marca);

    List<Veicolo> findByMarcaAndModello(String marca, String modello);

    List<Veicolo> findByAlimentazione(String alimentazione);

    List<Veicolo> findByAnnoProduzione(Integer annoProduzioneMinimo, Integer annoProduzioneMassimo);

    List<Veicolo> findByPrezzo(Double prezzoMinimo, Double prezzoMassimo);

    List<Veicolo> findByPotenza(Integer potenzaMinima, Integer potenzaMassima);

    List<Veicolo> findByChilometraggio(Integer chilometraggioMinimo, Integer chilometraggioMassimo);

    void registraVendita(long idVeicolo, Utente acquirente);
}
