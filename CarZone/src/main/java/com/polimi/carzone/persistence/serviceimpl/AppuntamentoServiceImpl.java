package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;
import com.polimi.carzone.exception.CredenzialiNonValideException;
import com.polimi.carzone.exception.UtenteNonTrovatoException;
import com.polimi.carzone.exception.VeicoloNonTrovatoException;
import com.polimi.carzone.model.Appuntamento;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
@Transactional
@RequiredArgsConstructor
public class AppuntamentoServiceImpl implements AppuntamentoService {

    private final AppuntamentoRepository appuntamentoRepo;
    private final UtenteRepository utenteRepo;
    private final VeicoloRepository veicoloRepo;

    @Override
    public void prenota(PrenotazioneRequestDTO request) {
        Map<String,String> errori = new TreeMap<>();

        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(request.getDataOra().isBefore(LocalDateTime.now()) || request.getDataOra().isEqual(LocalDateTime.now())) {
            errori.put("dataOra", "La data e l'ora devono essere successive a quella attuale");
        }

        if(request.getIdVeicolo() <= 0) {
            errori.put("idVeicolo", "L'id del veicolo non è valido");
        }

        if(request.getIdCliente() <= 0) {
            errori.put("idCliente", "L'id del cliente non è valido");
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Utente> cliente = utenteRepo.findById(request.getIdCliente());
        Optional<Veicolo> veicolo = veicoloRepo.findById(request.getIdVeicolo());
        if (cliente.isEmpty()){
            throw new UtenteNonTrovatoException("Cliente non trovato");
        }
        if(veicolo.isEmpty()){
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        Appuntamento appuntamento = new Appuntamento();
        appuntamento.setDataOra(request.getDataOra());
        appuntamento.setCliente(cliente.get());
        appuntamento.setVeicolo(veicolo.get());
        appuntamentoRepo.save(appuntamento);
    }
}
