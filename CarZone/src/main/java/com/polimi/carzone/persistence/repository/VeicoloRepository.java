package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {
}
