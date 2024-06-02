package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.ModificaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloManagerResponseDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.strategy.RicercaStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VeicoloService {

    void aggiungiVeicolo(AggiuntaVeicoloRequestDTO request, MultipartFile immagine) throws IOException;

    DettagliVeicoloManagerResponseDTO recuperaDettagli(Long idVeicolo) throws IOException;

    List<VeicoloResponseDTO> findAll() throws IOException;

    Veicolo findByTarga(String targa);

    Veicolo findById(long id);

List<VeicoloResponseDTO> convertiVeicoliInVeicoliResponse(List<Veicolo> veicoliTrovati) throws IOException;

    List<Veicolo> findByMarca(String marca);

    List<Veicolo> findByMarcaAndModello(String marca, String modello);

    List<Veicolo> findByAlimentazione(String alimentazione);

    List<Veicolo> findByAnnoProduzione(Integer annoProduzioneMinimo, Integer annoProduzioneMassimo);

    List<Veicolo> findByPrezzo(Double prezzoMinimo, Double prezzoMassimo);

    List<Veicolo> findByPotenza(Integer potenzaMinima, Integer potenzaMassima);

    List<Veicolo> findByChilometraggio(Integer chilometraggioMinimo, Integer chilometraggioMassimo);

    void registraVendita(Long idVeicolo, Utente acquirente);

    List<DettagliVeicoloManagerResponseDTO> findAllConDettagli() throws IOException;

    void eliminaVeicolo(Long idVeicolo);

    void modificaVeicolo(Long idVeicolo, ModificaVeicoloRequestDTO request);

    List<DettagliVeicoloManagerResponseDTO> findAllDisponibili() throws IOException;

    List<DettagliVeicoloManagerResponseDTO> findAllDisponibiliESelezionato(Long idVeicoloSelezionato) throws IOException;

    List<Long> estraiIdDaFindAllDisponibili(List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili);
}
