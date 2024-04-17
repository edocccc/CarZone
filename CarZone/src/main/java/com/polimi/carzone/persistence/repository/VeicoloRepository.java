package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {
    Optional<Veicolo> findById(long id);
}
