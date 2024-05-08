package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {

    Optional<Appuntamento> findById(long id);

    Optional<List<Appuntamento>> findByDipendente_Id(long idDipendente);

    Optional<List<Appuntamento>> findByDipendente_IdAndRecensioneVotoNotNull(long idDipendente);

    Optional<List<Appuntamento>> findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(long idDipendente);

    Optional<List<Appuntamento>> findByDipendenteIsNull();
}
