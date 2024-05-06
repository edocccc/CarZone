package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;
import com.polimi.carzone.dto.request.PresaInCaricoRequestDTO;
import com.polimi.carzone.dto.response.AppuntamentoResponseDTO;
import com.polimi.carzone.dto.response.PresaInCaricoResponseDTO;
import com.polimi.carzone.dto.response.ValutazioneMediaResponseDTO;
import com.polimi.carzone.exception.AppuntamentoNonTrovatoException;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public List<AppuntamentoResponseDTO> trovaAppuntamentiDipendente(long idDipendente) {
        Map<String,String> errori = new TreeMap<>();
        List<AppuntamentoResponseDTO> appuntamenti = new ArrayList<>();

        if(idDipendente <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_Id(idDipendente);
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                AppuntamentoResponseDTO appuntamentoDipendente = new AppuntamentoResponseDTO();
                appuntamentoDipendente.setId(appuntamento.getId());
                appuntamentoDipendente.setDataOra(appuntamento.getDataOra());
                appuntamentoDipendente.setNomeCliente(appuntamento.getCliente().getNome());
                appuntamentoDipendente.setCognomeCliente(appuntamento.getCliente().getCognome());
                appuntamentoDipendente.setTargaVeicolo(appuntamento.getVeicolo().getTarga());
                appuntamentoDipendente.setMarcaVeicolo(appuntamento.getVeicolo().getMarca());
                appuntamentoDipendente.setModelloVeicolo(appuntamento.getVeicolo().getModello());
                appuntamentoDipendente.setDataPassata(appuntamento.getDataOra().isBefore(LocalDateTime.now()));
                appuntamenti.add(appuntamentoDipendente);
            }
            return appuntamenti;
        } else {
            throw new AppuntamentoNonTrovatoException("Nessun appuntamento trovato");
        }
    }

    @Override
    public ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(long idDipendente) {
        Map<String,String> errori = new TreeMap<>();
        ValutazioneMediaResponseDTO valutazioneMediaResponse = new ValutazioneMediaResponseDTO();
        double valutazioneMedia = 0.0;

        if(idDipendente <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNull(idDipendente);
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            double sommaValutazioni = 0.0;
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                if(appuntamento.getRecensioneVoto() != null){
                    sommaValutazioni += appuntamento.getRecensioneVoto();
                }
            }
            valutazioneMedia = sommaValutazioni / appuntamentiTrovati.get().size();
            valutazioneMediaResponse.setValutazioneMedia(valutazioneMedia);
            return valutazioneMediaResponse;
        } else {
            throw new AppuntamentoNonTrovatoException("Nessuna valutazione rilevata");
        }
    }

    @Override
    public List<AppuntamentoResponseDTO> trovaAppuntamentiLiberi() {
        List<AppuntamentoResponseDTO> appuntamentiLiberi = new ArrayList<>();
        Optional<List<Appuntamento>> appuntamenti = appuntamentoRepo.findByDipendenteIsNull();
        if(appuntamenti.isPresent() && !appuntamenti.get().isEmpty()){
            for(Appuntamento appuntamento : appuntamenti.get()){
                AppuntamentoResponseDTO appuntamentoLiberi = new AppuntamentoResponseDTO();
                appuntamentoLiberi.setId(appuntamento.getId());
                appuntamentoLiberi.setDataOra(appuntamento.getDataOra());
                appuntamentoLiberi.setNomeCliente(appuntamento.getCliente().getNome());
                appuntamentoLiberi.setCognomeCliente(appuntamento.getCliente().getCognome());
                appuntamentoLiberi.setTargaVeicolo(appuntamento.getVeicolo().getTarga());
                appuntamentoLiberi.setMarcaVeicolo(appuntamento.getVeicolo().getMarca());
                appuntamentoLiberi.setModelloVeicolo(appuntamento.getVeicolo().getModello());
                appuntamentiLiberi.add(appuntamentoLiberi);
            }
            return appuntamentiLiberi;
        } else {
            throw new AppuntamentoNonTrovatoException("Nessun appuntamento libero trovato");
        }
    }

    @Override
    public void prendiInCarico(PresaInCaricoRequestDTO request) {
        Map<String,String> errori = new TreeMap<>();

        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(request.getIdAppuntamento() <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
        }

        if(request.getIdDipendente() <= 0){
            errori.put("idDipendente", "L'id del dipendente non è valido");
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(request.getIdAppuntamento());
        Optional<Utente> dipendente = utenteRepo.findById(request.getIdDipendente());
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        if(dipendente.isEmpty()){
            throw new UtenteNonTrovatoException("Dipendente non trovato");
        }

        appuntamento.get().setDipendente(dipendente.get());
        appuntamentoRepo.save(appuntamento.get());
    }

    @Override
    public long trovaIdVeicolo(long idAppuntamento) {
        Map<String,String> errori = new TreeMap<>();

        if(idAppuntamento <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        if(appuntamento.isPresent()){
            return appuntamento.get().getVeicolo().getId();
        } else {
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
    }

    @Override
    public long trovaIdCliente(long idAppuntamento) {
        Map<String,String> errori = new TreeMap<>();

        if(idAppuntamento <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        if(appuntamento.isPresent()){
            return appuntamento.get().getCliente().getId();
        } else {
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
    }
}
