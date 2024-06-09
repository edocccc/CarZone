package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.ModificaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloManagerResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VeicoloService {
    //la classe service è un'interfaccia che definisce i metodi che, una volta implementati, verranno chiamati dal controller
    //i metodi dichiarati insieme alla loro implementazione comporranno la logica di business dell'applicazione riguardante la gestione dei veicoli

    //metodo per il manager che a partire da una richiesta e un file immagine, aggiunge un veicolo
    void aggiungiVeicolo(AggiuntaVeicoloRequestDTO request, MultipartFile immagine) throws IOException;

    //metodo per il manager che a partire da un id, trova i dettagli di un veicolo
    DettagliVeicoloManagerResponseDTO recuperaDettagli(Long idVeicolo) throws IOException;

    //metodo che trova tutti i veicoli
    List<VeicoloResponseDTO> findAll() throws IOException;

    //metodo che a partire da una targa, trova un veicolo
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Veicolo findByTarga(String targa);

    //metodo che a partire da un id, trova un veicolo
    Veicolo findById(long id);

    //metodo che a partire da una lista di veicoli, li converte in una lista di veicoliResponse
    List<VeicoloResponseDTO> convertiVeicoliInVeicoliResponse(List<Veicolo> veicoliTrovati) throws IOException;

    //metodo che a partire da una marca, trova i veicoli corrispondenti
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    List<Veicolo> findByMarca(String marca);

    //metodo che a partire da una marca e un modello, trova i veicoli corrispondenti
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    List<Veicolo> findByMarcaAndModello(String marca, String modello);

    //metodo che a partire da un tipo di alimentazione, trova i veicoli corrispondenti
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    List<Veicolo> findByAlimentazione(String alimentazione);

    //metodo che a partire da un anno di produzione minimo e uno massimo, trova i veicoli corrispondenti
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    List<Veicolo> findByAnnoProduzione(Integer annoProduzioneMinimo, Integer annoProduzioneMassimo);

    //metodo che a partire da un prezzo minimo e uno massimo, trova i veicoli corrispondenti
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    List<Veicolo> findByPrezzo(Double prezzoMinimo, Double prezzoMassimo);

    //metodo che a partire da una potenza minima e una massima, trova i veicoli corrispondenti
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    List<Veicolo> findByPotenza(Integer potenzaMinima, Integer potenzaMassima);

    //metodo che a partire da un chilometraggio minimo e uno massimo, trova i veicoli corrispondenti
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    List<Veicolo> findByChilometraggio(Integer chilometraggioMinimo, Integer chilometraggioMassimo);

    //metodo che a partire da un id di un veicolo e un acquirente, registra la vendita del veicolo
    void registraVendita(Long idVeicolo, Utente acquirente);

    //metodo per il manager che trova tutti i veicoli con i dettagli
    List<DettagliVeicoloManagerResponseDTO> findAllConDettagli() throws IOException;

    //metodo per il manager che a partire da un id, elimina un veicolo
    void eliminaVeicolo(Long idVeicolo);

    //metodo per il manager che a partire da un id e una richiesta, modifica un veicolo
    void modificaVeicolo(Long idVeicolo, ModificaVeicoloRequestDTO request);

    //metodo per il manager che trova tutti i veicoli disponibili
    List<DettagliVeicoloManagerResponseDTO> findAllDisponibili() throws IOException;

    //metodo per il manager che trova tutti i veicoli disponibili e anche il veicolo selezionato
    List<DettagliVeicoloManagerResponseDTO> findAllDisponibiliESelezionato(Long idVeicoloSelezionato) throws IOException;

    //metodo per il manager che a partire da una lista di veicoli disponibili, estrae gli id
    List<Long> estraiIdDaFindAllDisponibili(List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili);
}
