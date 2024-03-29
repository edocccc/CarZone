package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long>{

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByUsernameAndPassword(String username, String password);
}
