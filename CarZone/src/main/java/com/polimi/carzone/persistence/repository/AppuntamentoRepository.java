package com.polimi.carzone.persistence.repository;

import com.polimi.carzone.model.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {
    //l'interfaccia si occupa di dichiarare i metodi per interagire con il database
    //i metodi ritornano un Optional per gestire il caso in cui non venga trovato nulla

    //metodo per trovare un appuntamento tramite id
    Optional<Appuntamento> findById(long id);

    //metodo per trovare gli appuntamenti il cui esito non è stato registrato, tramite id del dipendente
    Optional<List<Appuntamento>> findByDipendente_IdAndEsitoRegistratoIsFalse(long idDipendente);

    //metodo per trovare gli appuntamenti in cui i parametri recensioneVoto e recensioneTesto sono null, tramite id del dipeendente
    Optional<List<Appuntamento>> findByCliente_IdAndRecensioneVotoIsNullAndRecensioneTestoIsNull(long idDipendente);

    //metodo per trovare gli appuntamenti in cui i parametri recensioneVoto è null, tramite id del dipendente
    Optional<List<Appuntamento>> findByDipendente_IdAndRecensioneVotoNotNull(long idDipendente);

    //metodo per trovare gli appuntamenti in cui i parametri recensioneVoto e recensioneTesto non sono null, tramite id del dipendente
    Optional<List<Appuntamento>> findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(long idDipendente);

    //metodo per trovare gli appuntamenti in cui il dipendente è null (non assegnati)
    Optional<List<Appuntamento>> findByDipendenteIsNull();

    //metodo per trovare gli appuntamenti la cui vendita non sia stata registrata, tramite id del veicolo
    Optional<List<Appuntamento>> findByVeicolo_IdAndEsitoRegistratoIsFalse(long idVeicolo);

    //metodo per trovare gli appuntamenti in cui i parametri recensioneVoto e recensioneTesto non sono null, tramite id del cliente
    Optional<List<Appuntamento>> findByCliente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(long idCliente);
}
