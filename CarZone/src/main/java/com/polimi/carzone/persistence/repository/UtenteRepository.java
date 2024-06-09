package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long>{
    //l'interfaccia si occupa di dichiarare i metodi per interagire con il database
    //i metodi ritornano un Optional per gestire il caso in cui non venga trovato nulla

    //metodo per trovare un utente tramite username
    Optional<Utente> findByUsername(String username);

    //metodo per trovare un utente tramite id
    Optional<Utente> findById(long id);

    //metodo per trovare gli utenti che hanno un determinato ruolo
    List<Utente> findByRuolo(Ruolo ruolo);

}
