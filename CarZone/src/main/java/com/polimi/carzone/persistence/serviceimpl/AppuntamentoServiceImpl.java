package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.dto.response.*;
import com.polimi.carzone.exception.*;
import com.polimi.carzone.model.Appuntamento;
import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import com.polimi.carzone.persistence.service.UtenteService;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.security.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AppuntamentoServiceImpl implements AppuntamentoService {

    private final AppuntamentoRepository appuntamentoRepo;
    private final UtenteRepository utenteRepo;
    private final VeicoloRepository veicoloRepo;
    private final TokenUtil tokenUtil;
    private final VeicoloService veicoloService;
    private final UtenteService utenteService;

    @Override
    public void prenota(PrenotazioneRequestDTO request, String token) {
        Map<String,String> errori = new TreeMap<>();

        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(request.getDataOra() == null || request.getDataOra().toLocalDate().isBefore(LocalDate.now()) || request.getDataOra().toLocalDate().isEqual(LocalDate.now())) {
            errori.put("dataOra", "La data deve essere almeno il giorno successivo a quello attuale");
            throw new CredenzialiNonValideException(errori);
        }

        int ora = request.getDataOra().getHour();
        int minuto = request.getDataOra().getMinute();

        if(ora < 9 || ora > 17 || (ora == 17 && minuto > 0)) {
            errori.put("dataOra", "Gli appuntamenti possono essere prenotati solo dalle 9:00 alle 17:00");
        }

        if(request.getIdVeicolo() == null || request.getIdVeicolo() <= 0) {
            errori.put("idVeicolo", "L'id del veicolo non è valido");
        }

        if(request.getIdCliente() == null || request.getIdCliente() <= 0) {
            errori.put("idCliente", "L'id del cliente non è valido");
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili = veicoloService.findAllDisponibili();
        List<Long> veicoliDisponibiliId = veicoloService.estraiIdDaFindAllDisponibili(veicoliDisponibili);
        if(!veicoliDisponibiliId.contains(request.getIdVeicolo())){
            throw new VeicoloNonTraDisponibiliException("Il veicolo non è disponibile");
        }

        if(request.getIdCliente() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del cliente non corrisponde all'id dell'utente loggato");
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
        appuntamento.setEsitoRegistrato(false);
        appuntamentoRepo.save(appuntamento);
    }

    @Override
    public List<AppuntamentoResponseDTO> trovaAppuntamentiDipendente(Long idDipendente, String token) {
        Map<String,String> errori = new TreeMap<>();
        List<AppuntamentoResponseDTO> appuntamenti = new ArrayList<>();

        if(idDipendente == null || idDipendente <= 0){
            errori.put("idDipendente", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        if(idDipendente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }

        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndEsitoRegistratoIsFalse(idDipendente);
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()) {
            for (Appuntamento appuntamento : appuntamentiTrovati.get()) {
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
        }
        return appuntamenti;
    }

    @Override
    public List<AppuntamentoConRecensioneResponseDTO> trovaAppuntamentiCliente(Long idCliente, String token) {
        Map<String,String> errori = new TreeMap<>();
        List<AppuntamentoConRecensioneResponseDTO> appuntamenti = new ArrayList<>();

        if(idCliente == null || idCliente <= 0){
            errori.put("idCliente", "L'id del cliente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        if(idCliente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del cliente non corrisponde all'id dell'utente loggato");
        }

        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByCliente_IdAndRecensioneVotoIsNullAndRecensioneTestoIsNull(idCliente);
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()) {
            for (Appuntamento appuntamento : appuntamentiTrovati.get()) {
                AppuntamentoConRecensioneResponseDTO appuntamentoCliente = new AppuntamentoConRecensioneResponseDTO();
                appuntamentoCliente.setId(appuntamento.getId());
                appuntamentoCliente.setDataOra(appuntamento.getDataOra());
                appuntamentoCliente.setNomeCliente(appuntamento.getCliente().getNome());
                appuntamentoCliente.setCognomeCliente(appuntamento.getCliente().getCognome());
                if(appuntamento.getDipendente() != null) {
                    appuntamentoCliente.setNomeDipendente(appuntamento.getDipendente().getNome());
                    appuntamentoCliente.setCognomeDipendente(appuntamento.getDipendente().getCognome());
                }
                appuntamentoCliente.setTargaVeicolo(appuntamento.getVeicolo().getTarga());
                appuntamentoCliente.setMarcaVeicolo(appuntamento.getVeicolo().getMarca());
                appuntamentoCliente.setModelloVeicolo(appuntamento.getVeicolo().getModello());
                appuntamentoCliente.setDataPassata(appuntamento.getDataOra().isBefore(LocalDateTime.now()));
                appuntamenti.add(appuntamentoCliente);
            }
        }
        return appuntamenti;
    }

    @Override
    public ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente, String token) {
        Map<String,String> errori = new TreeMap<>();
        ValutazioneMediaResponseDTO valutazioneMediaResponse = new ValutazioneMediaResponseDTO();
        double valutazioneMedia = 0.0;

        if(idDipendente == null || idDipendente <= 0){
            errori.put("idDipendente", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        Ruolo ruoloUtente = tokenUtil.getUtenteFromToken(token.substring(7)).getRuolo();
        if(ruoloUtente != Ruolo.MANAGER && idDipendente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
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
        } else {
            valutazioneMediaResponse.setValutazioneMedia(0.0);
        }
        return valutazioneMediaResponse;
    }

    @Override
    public ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente) {
        Map<String,String> errori = new TreeMap<>();
        ValutazioneMediaResponseDTO valutazioneMediaResponse = new ValutazioneMediaResponseDTO();
        double valutazioneMedia = 0.0;

        if(idDipendente == null || idDipendente <= 0){
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
        } else {
            valutazioneMediaResponse.setValutazioneMedia(0.0);
        }
        return valutazioneMediaResponse;
    }

    @Override
    public List<AppuntamentoResponseDTO> trovaAppuntamentiLiberi() {
        List<AppuntamentoResponseDTO> appuntamentiLiberi = new ArrayList<>();
        Optional<List<Appuntamento>> appuntamenti = appuntamentoRepo.findByDipendenteIsNull();
        if(appuntamenti.isPresent() && !appuntamenti.get().isEmpty()) {
            for (Appuntamento appuntamento : appuntamenti.get()) {
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
        }
        return appuntamentiLiberi;
    }

    @Override
    public void prendiInCarico(PresaInCaricoRequestDTO request, String token) {
        Map<String,String> errori = new TreeMap<>();

        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(request.getIdAppuntamento() == null || request.getIdAppuntamento() <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
        }

        if(request.getIdDipendente() == null || request.getIdDipendente() <= 0){
            errori.put("idDipendente", "L'id del dipendente non è valido");
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        if(request.getIdDipendente() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(request.getIdAppuntamento());
        Optional<Utente> dipendente = utenteRepo.findById(request.getIdDipendente());
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        if(dipendente.isEmpty()){
            throw new UtenteNonTrovatoException("Dipendente non trovato");
        }
        if(appuntamento.get().getDipendente() != null){
            throw new AppuntamentoPresoInCaricoException("L'appuntamento è già stato preso in carico");
        }
        if(appuntamento.get().getDataOra().isBefore(LocalDateTime.now())){
            throw new AppuntamentoPassatoException("L'appuntamento è già passato");
        }

        appuntamento.get().setDipendente(dipendente.get());
        appuntamentoRepo.save(appuntamento.get());
    }

    @Override
    public long trovaIdVeicolo(Long idAppuntamento, String token) {
        Map<String,String> errori = new TreeMap<>();

        if(idAppuntamento == null || idAppuntamento <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        if(appuntamento.get().getDipendente() != null && appuntamento.get().getDipendente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }
        return appuntamento.get().getVeicolo().getId();
    }

    @Override
    public long trovaIdCliente(Long idAppuntamento, String token) {
        Map<String,String> errori = new TreeMap<>();

        if(idAppuntamento == null || idAppuntamento <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        if(appuntamento.isPresent()){
            if(appuntamento.get().getDipendente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
                throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
            }
            return appuntamento.get().getCliente().getId();
        } else {
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
    }

    @Override
    public List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente, String token) {
        Map<String,String> errori = new TreeMap<>();
        List<RecensioneResponseDTO> recensioni = new ArrayList<>();

        if(idDipendente == null || idDipendente <= 0){
            errori.put("id", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        Ruolo ruoloUtente = tokenUtil.getUtenteFromToken(token.substring(7)).getRuolo();
        if(ruoloUtente != Ruolo.MANAGER && idDipendente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }

        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(idDipendente);
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                recensioni.add(new RecensioneResponseDTO(
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        appuntamento.getRecensioneVoto(),
                        appuntamento.getRecensioneTesto()
                ));
            }
        }
        return recensioni;
    }

    @Override
    public List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente) {
        Map<String,String> errori = new TreeMap<>();
        List<RecensioneResponseDTO> recensioni = new ArrayList<>();

        if(idDipendente == null || idDipendente <= 0){
            errori.put("id", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(idDipendente);
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                recensioni.add(new RecensioneResponseDTO(
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        appuntamento.getRecensioneVoto(),
                        appuntamento.getRecensioneTesto()
                ));
            }
        }
        return recensioni;
    }

    @Override
    public List<DipendenteConRecensioneDTO> trovaDipendentiConRecensioni() {
        List<DipendenteConRecensioneDTO> dipendentiConRecensioni = new ArrayList<>();
        List<Utente> dipendenti = utenteRepo.findByRuolo(Ruolo.DIPENDENTE);
        for(Utente dipendente : dipendenti){
            List<RecensioneResponseDTO> recensioni = trovaRecensioniDipendente(dipendente.getId());
            if(!recensioni.isEmpty()){
                dipendentiConRecensioni.add(new DipendenteConRecensioneDTO(
                        dipendente.getNome(),
                        dipendente.getCognome(),
                        calcolaValutazioneMediaDipendente(dipendente.getId()),
                        recensioni
                ));
            }
        }
        return dipendentiConRecensioni;
    }

    @Override
    public void registraVendita(Long idAppuntamento, boolean venditaConclusa, String token) {
        Map<String,String> errori = new TreeMap<>();

        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        Ruolo ruoloUtente = tokenUtil.getUtenteFromToken(token.substring(7)).getRuolo();
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        if(ruoloUtente != Ruolo.MANAGER && appuntamento.get().getDipendente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }
        if(appuntamento.get().getDataOra().isAfter(LocalDateTime.now())){
            throw new AppuntamentoNonSvoltoException("L'appuntamento non è ancora stato svolto");
        }
        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }
        if(appuntamento.get().getDipendente() == null){
            throw new AppuntamentoNonAssegnatoException("L'appuntamento non è stato preso in carico da nessun dipendente");
        }

        appuntamento.get().setEsitoRegistrato(true);
        appuntamentoRepo.save(appuntamento.get());
    }

    @Override
    public List<AppuntamentoManagerResponseDTO> trovaAppuntamentiPerManager() {
        List<AppuntamentoManagerResponseDTO> appuntamentiPerManager = new ArrayList<>();
        List<Appuntamento> appuntamenti = appuntamentoRepo.findAll();
        for(Appuntamento appuntamento : appuntamenti){
            if(appuntamento.getDipendente() != null){
                appuntamentiPerManager.add(new AppuntamentoManagerResponseDTO(
                        appuntamento.getId(),
                        appuntamento.getDataOra(),
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        appuntamento.getDipendente().getNome(),
                        appuntamento.getDipendente().getCognome(),
                        appuntamento.getVeicolo().getTarga(),
                        appuntamento.getVeicolo().getMarca(),
                        appuntamento.getVeicolo().getModello(),
                        appuntamento.isEsitoRegistrato(),
                        appuntamento.getDataOra().isBefore(LocalDateTime.now())

                ));
            } else {
                appuntamentiPerManager.add(new AppuntamentoManagerResponseDTO(
                        appuntamento.getId(),
                        appuntamento.getDataOra(),
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        null,
                        null,
                        appuntamento.getVeicolo().getTarga(),
                        appuntamento.getVeicolo().getMarca(),
                        appuntamento.getVeicolo().getModello(),
                        appuntamento.isEsitoRegistrato(),
                        appuntamento.getDataOra().isBefore(LocalDateTime.now())
                ));
            }

        }
        return appuntamentiPerManager;
    }

    @Override
    public void prenotaPerManager(PrenotazioneManagerRequestDTO request) {
        Map<String,String> errori = new TreeMap<>();

        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(request.getDataOra() == null || request.getDataOra().isBefore(LocalDateTime.now()) || request.getDataOra().isEqual(LocalDateTime.now())) {
            errori.put("dataOra", "La data e l'ora devono essere successive a quella attuale");
        }

        if(request.getIdVeicolo() == null || request.getIdVeicolo() <= 0) {
            errori.put("idVeicolo", "L'id del veicolo non è valido");
        }

        if(request.getIdCliente() == null || request.getIdCliente() <= 0) {
            errori.put("idCliente", "L'id del cliente non è valido");
        }

        if(request.getIdDipendente() == null || request.getIdDipendente() < 0) {
            errori.put("idDipendente", "L'id del dipendente non è valido");
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Utente> cliente = utenteRepo.findById(request.getIdCliente());
        Optional<Veicolo> veicolo = veicoloRepo.findById(request.getIdVeicolo());
        Optional<Utente> dipendente = Optional.empty();
        if(request.getIdDipendente() != 0){
            dipendente = utenteRepo.findById(request.getIdDipendente());
            if(dipendente.isEmpty()){
                throw new UtenteNonTrovatoException("Dipendente non trovato");
            }
        }
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
        if(request.getIdDipendente() != 0){
            appuntamento.setDipendente(dipendente.get());
        }
        appuntamento.setEsitoRegistrato(false);
        appuntamentoRepo.save(appuntamento);
    }

    @Override
    public void eliminaAppuntamento(Long idAppuntamento) {
        Map<String,String> errori = new TreeMap<>();

        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }

        appuntamentoRepo.delete(appuntamento.get());
    }

    @Override
    public void modificaAppuntamento(Long idAppuntamento, ModificaAppuntamentoRequestDTO request) {
        Map<String,String> errori = new TreeMap<>();

        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(request.getDataOra() == null || request.getDataOra().isBefore(LocalDateTime.now()) || request.getDataOra().isEqual(LocalDateTime.now())) {
            errori.put("dataOra", "La data e l'ora devono essere successive a quella attuale");
        }

        if(request.getIdVeicolo() == null || request.getIdVeicolo() <= 0) {
            errori.put("idVeicolo", "L'id del veicolo non è valido");
        }

        if(request.getIdCliente() == null || request.getIdCliente() <= 0) {
            errori.put("idCliente", "L'id del cliente non è valido");
        }

        if(request.getIdDipendente() != null && request.getIdDipendente() < 0) {
            errori.put("idDipendente", "L'id del dipendente non è valido");
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        Optional<Utente> cliente = utenteRepo.findById(request.getIdCliente());
        Optional<Veicolo> veicolo = veicoloRepo.findById(request.getIdVeicolo());
        Optional<Utente> dipendente = Optional.empty();
        if(request.getIdDipendente() != null){
            dipendente = utenteRepo.findById(request.getIdDipendente());
            if(dipendente.isEmpty()){
                throw new UtenteNonTrovatoException("Dipendente non trovato");
            }
        }
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }
        if(cliente.isEmpty()){
            throw new UtenteNonTrovatoException("Cliente non trovato");
        }

        if(veicolo.isEmpty()){
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }

        appuntamento.get().setDataOra(request.getDataOra());
        appuntamento.get().setCliente(cliente.get());
        appuntamento.get().setVeicolo(veicolo.get());
        if(request.getIdDipendente() != null){
            appuntamento.get().setDipendente(dipendente.get());
        }
        appuntamentoRepo.save(appuntamento.get());
    }

    @Override
    public void lasciaRecensione(LasciaRecensioneRequestDTO request,String token) {
        Map<String,String> errori = new TreeMap<>();

        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(request.getIdAppuntamento()==null || request.getIdAppuntamento() <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
        }

        if(request.getVotoRecensione()==null || request.getVotoRecensione() < 1 || request.getVotoRecensione() > 5){
            errori.put("voto", "Il voto deve essere compreso tra 1 e 5");
        }

        if(request.getTestoRecensione().isEmpty() || request.getTestoRecensione().isBlank()){
            errori.put("testo", "Il testo non può essere vuoto");
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(request.getIdAppuntamento());
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }

        if(appuntamento.get().getCliente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'appuntamento non è tuo");
        }

        if(!appuntamento.get().getDataOra().isBefore(LocalDateTime.now())) {
            throw new AppuntamentoNonSvoltoException("Appuntamento non ancora svolto");
        }

        if(appuntamento.get().getRecensioneVoto() != null || appuntamento.get().getRecensioneTesto() != null){
            throw new RecensioneGiaLasciataException("Recensione già lasciata");
        }

        if(appuntamento.get().getDipendente() == null){
            throw new DipendenteNonAssegnatoException("Dipendente non assegnato");
        }

        appuntamento.get().setRecensioneVoto(request.getVotoRecensione());
        appuntamento.get().setRecensioneTesto(request.getTestoRecensione());
        appuntamentoRepo.save(appuntamento.get());
    }

    @Override
    public List<RecensioneClienteResponseDTO> trovaRecensioniCliente(Long idCliente, String token) {
        Map<String,String> errori = new TreeMap<>();
        List<RecensioneClienteResponseDTO> recensioni = new ArrayList<>();

        if(idCliente == null || idCliente <= 0){
            errori.put("idCliente", "L'id del cliente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        if(idCliente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del cliente non corrisponde all'id dell'utente loggato");
        }

        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByCliente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(idCliente);
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                recensioni.add(new RecensioneClienteResponseDTO(
                        appuntamento.getDipendente().getNome(),
                        appuntamento.getDipendente().getCognome(),
                        appuntamento.getRecensioneVoto(),
                        appuntamento.getRecensioneTesto()
                ));
            }
            return recensioni;
        } else {
            throw new AppuntamentoNonTrovatoException("Nessuna recensione trovata");
        }
    }

    @Override
    public AppuntamentoModificaResponseDTO trovaPerModifica(Long idAppuntamento, String token) {
        Map<String,String> errori = new TreeMap<>();
        AppuntamentoModificaResponseDTO appuntamentoModifica = new AppuntamentoModificaResponseDTO();

        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }

        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }

        appuntamentoModifica.setDataOra(appuntamento.get().getDataOra());
        appuntamentoModifica.setIdCliente(appuntamento.get().getCliente().getId());
        appuntamentoModifica.setNomeCliente(appuntamento.get().getCliente().getNome());
        appuntamentoModifica.setCognomeCliente(appuntamento.get().getCliente().getCognome());
        appuntamentoModifica.setIdVeicolo(appuntamento.get().getVeicolo().getId());
        appuntamentoModifica.setTargaVeicolo(appuntamento.get().getVeicolo().getTarga());
        appuntamentoModifica.setMarcaVeicolo(appuntamento.get().getVeicolo().getMarca());
        appuntamentoModifica.setModelloVeicolo(appuntamento.get().getVeicolo().getModello());
        if(appuntamento.get().getDipendente() != null){
            appuntamentoModifica.setIdDipendente(appuntamento.get().getDipendente().getId());
            appuntamentoModifica.setNomeDipendente(appuntamento.get().getDipendente().getNome());
            appuntamentoModifica.setCognomeDipendente(appuntamento.get().getDipendente().getCognome());
        }
        return appuntamentoModifica;
    }
}
