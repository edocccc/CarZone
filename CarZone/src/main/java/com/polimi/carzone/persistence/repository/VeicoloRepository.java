package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Alimentazione;
import com.polimi.carzone.model.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {
    //l'interfaccia si occupa di dichiarare i metodi per interagire con il database
    //i metodi ritornano un Optional per gestire il caso in cui non venga trovato nulla

    //metodo per trovare un veicolo tramite id
    Optional<Veicolo> findById(long id);

    //metodo per trovare un veicolo tramite targa
    Optional<Veicolo> findByTarga(String targa);

    //metodo per trovare i veicoli che hanno una determinata marca
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Optional<List<Veicolo>> findByMarca(String marca);

    //metodo per trovare i veicoli che hanno una determinata marca e modello
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Optional<List<Veicolo>> findByMarcaAndModello(String marca, String modello);

    //metodo per trovare i veicoli che hanno una determinata alimentazione
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Optional<List<Veicolo>> findByAlimentazione(Alimentazione alimentazione);

    //metodo per trovare i veicoli che hanno un anno di produzione compreso tra due valori
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Optional<List<Veicolo>> findByAnnoProduzioneBetween(int annoProduzioneMinimo, int annoProduzioneMassimo);

    //metodo per trovare i veicoli che hanno un prezzo compreso tra due valori
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Optional<List<Veicolo>> findByPrezzoBetween(double prezzoMinimo, double prezzoMassimo);

    //metodo per trovare i veicoli che hanno una potenza compresa tra due valori
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Optional<List<Veicolo>> findByPotenzaCvBetween(int potenzaMinima, int potenzaMassima);

    //metodo per trovare i veicoli che hanno un chilometraggio compreso tra due valori
    //il metodo fa parte dell'implementazione di una delle strategie di ricerca implementate tramite pattern strategy
    Optional<List<Veicolo>> findByChilometraggioBetween(int chilometraggioMinimo, int chilometraggioMassimo);
}
