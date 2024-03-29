package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {
}
