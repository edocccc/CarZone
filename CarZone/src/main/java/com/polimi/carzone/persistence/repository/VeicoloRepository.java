package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Alimentazione;
import com.polimi.carzone.model.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {

    Optional<Veicolo> findById(long id);

    Optional<Veicolo> findByTarga(String targa);

    Optional<List<Veicolo>> findByMarca(String marca);

    Optional<List<Veicolo>> findByMarcaAndModello(String marca, String modello);

    Optional<List<Veicolo>> findByAlimentazione(Alimentazione alimentazione);
}
