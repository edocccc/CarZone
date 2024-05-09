package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long>{

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findById(long id);

    Optional<Utente> findByUsernameAndPassword(String username, String password);

    List<Utente> findByRuolo(Ruolo ruolo);

    List<Utente> findByRuoloNot(Ruolo ruolo);
}
