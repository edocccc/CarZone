package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.ModificaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloManagerResponseDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.exception.*;
import com.polimi.carzone.model.Alimentazione;
import com.polimi.carzone.model.Appuntamento;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.state.implementation.Disponibile;
import com.polimi.carzone.state.implementation.Trattativa;
import com.polimi.carzone.state.implementation.Venduto;
import com.polimi.carzone.strategy.RicercaStrategy;
import com.polimi.carzone.strategy.implementation.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class VeicoloServiceImpl implements VeicoloService {

    private final VeicoloRepository veicoloRepo;
    private final AppuntamentoRepository appuntamentoRepo;
    private static final String FOLDER_PATH = "C:/Users/casca/Desktop/unigenerale/IngegneriaDS/progettofinale/Progetto/CarZone/src/main/resources/immagini/";


    @Override
    public void aggiungiVeicolo(AggiuntaVeicoloRequestDTO request, MultipartFile immagine) throws IOException {
        Map<String,String> errori = new TreeMap<>();
        if(request == null) {
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        if(immagine == null) {
            errori.put("immagine", "Devi inserire un'immagine");
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Veicolo> checkTarga = veicoloRepo.findByTarga(request.getTarga());
        if(checkTarga.isPresent()) {
            errori.put("targa", "Targa già presente nel sistema");
            throw new CredenzialiNonValideException(errori);
        }
        if(request.getTarga() == null || request.getTarga().isEmpty() || request.getTarga().isBlank()) {
            errori.put("targa", "Devi inserire una targa valida");
        }
        if(request.getMarca() == null || request.getMarca().isEmpty() || request.getMarca().isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
        }
        if(request.getModello() == null || request.getModello().isEmpty() || request.getModello().isBlank()) {
            errori.put("modello", "Devi inserire un modello valido");
        }
        if(request.getChilometraggio() == null || request.getChilometraggio() < 0) {
            errori.put("chilometraggio", "Devi inserire un chilometraggio valido");
        }
        if(request.getAnnoProduzione() == null || request.getAnnoProduzione() < 1900 || request.getAnnoProduzione() > LocalDateTime.now().getYear()) {
            errori.put("annoProduzione", "Devi inserire un anno di produzione valido");
        }
        if(request.getPotenzaCv() == null || request.getPotenzaCv() < 0) {
            errori.put("potenzaCv", "Devi inserire una potenza valida");
        }

        if(request.getPrezzo() == null || request.getPrezzo() < 0.0) {
            errori.put("prezzo", "Devi inserire un prezzo valido");
        }

        Alimentazione alimentazione = null;

        if(request.getAlimentazione() == null || request.getAlimentazione().isEmpty() || request.getAlimentazione().isBlank()) {
            errori.put("alimentazione", "Devi inserire un tipo di alimentazione valido");
        } else {
            alimentazione = switch (request.getAlimentazione()) {
                case "BENZINA" -> Alimentazione.BENZINA;
                case "DIESEL" -> Alimentazione.DIESEL;
                case "IBRIDA" -> Alimentazione.IBRIDA;
                case "GPL" -> Alimentazione.GPL;
                case "ELETTRICA" -> Alimentazione.ELETTRICA;
                default -> throw new AlimentazioneNonValidaException("Tipo di alimentazione non valido");
            };
        }

        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }

        Veicolo veicolo = new Veicolo();
        veicolo.setTarga(request.getTarga());
        veicolo.setMarca(request.getMarca());
        veicolo.setModello(request.getModello());
        veicolo.setChilometraggio(request.getChilometraggio());
        veicolo.setAnnoProduzione(request.getAnnoProduzione());
        veicolo.setPotenzaCv(request.getPotenzaCv());
        veicolo.setAlimentazione(alimentazione);
        veicolo.setPrezzo(request.getPrezzo());
        veicolo.setNomeImmagine(immagine.getOriginalFilename());
        veicolo.setTipoImmagine(immagine.getContentType());
        veicolo.setFilePath(FOLDER_PATH + immagine.getOriginalFilename());

        veicoloRepo.save(veicolo);
        File file = new File(FOLDER_PATH + immagine.getOriginalFilename());
        immagine.transferTo(file);
    }

    @Override
    public List<VeicoloResponseDTO> findAll() throws IOException {
        List<Veicolo> veicoli = veicoloRepo.findAll();
        List<VeicoloResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoli) {
            byte[] immagine = null;
            if(veicolo.getFilePath() != null) {
                immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
            }
            veicoliResponse.add(new VeicoloResponseDTO(
                    veicolo.getId(),
                    veicolo.getMarca(),
                    veicolo.getModello(),
                    veicolo.getPrezzo(),
                    checkStato(veicolo),
                    immagine
            ));
        }
        return veicoliResponse;
    }


    @Override
    public Veicolo findByTarga(String targa) {
        Map<String,String> errori = new TreeMap<>();
        if(targa == null || targa.isEmpty() || targa.isBlank()) {
            errori.put("targa", "Devi inserire una targa valida");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Veicolo> veicolo = veicoloRepo.findByTarga(targa);
        if(veicolo.isPresent()){
            return veicolo.get();
        } else {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
    }

    @Override
    public Veicolo findById(long id) {
        Map<String,String> errori = new TreeMap<>();
        if(id <= 0) {
            errori.put("id", "Id veicolo non valido");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Veicolo> veicolo = veicoloRepo.findById(id);
        if(veicolo.isPresent()){
            return veicolo.get();
        } else {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
    }

    @Override
    public List<VeicoloResponseDTO> convertiVeicoliInVeicoliResponse(List<Veicolo> veicoliTrovati) throws IOException {
        List<VeicoloResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoliTrovati) {
            byte[] immagine = null;
            if(veicolo.getFilePath() != null) {
                immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
            }
            veicoliResponse.add(new VeicoloResponseDTO(
                    veicolo.getId(),
                    veicolo.getMarca(),
                    veicolo.getModello(),
                    veicolo.getPrezzo(),
                    checkStato(veicolo),
                    immagine
            ));
        }
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
        }
        return veicoliResponse;
    }

    @Override
    public List<Veicolo> findByMarca(String marca) {
        Map<String,String> errori = new TreeMap<>();
        if(marca == null || marca.isEmpty() || marca.isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByMarca(marca);
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    @Override
    public List<Veicolo> findByMarcaAndModello(String marca,String modello) {
        Map<String,String> errori = new TreeMap<>();
        if(marca == null || marca.isEmpty() || marca.isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
        }
        if(modello == null || modello.isEmpty() || modello.isBlank()) {
            errori.put("modello", "Devi inserire un modello valido");
        }
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByMarcaAndModello(marca,modello);
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    @Override
    public List<Veicolo> findByAlimentazione(String alimentazione) {
        Map<String,String> errori = new TreeMap<>();
        if(alimentazione == null || alimentazione.isEmpty() || alimentazione.isBlank()) {
            errori.put("alimentazione", "Devi inserire una alimentazione valida");
            throw new CredenzialiNonValideException(errori);
        }
        Alimentazione alimentazioneEnum = switch (alimentazione) {
            case "BENZINA" -> Alimentazione.BENZINA;
            case "DIESEL" -> Alimentazione.DIESEL;
            case "IBRIDA" -> Alimentazione.IBRIDA;
            case "GPL" -> Alimentazione.GPL;
            case "ELETTRICA" -> Alimentazione.ELETTRICA;
            default -> throw new AlimentazioneNonValidaException("Tipo di alimentazione non valido");
        };
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByAlimentazione(alimentazioneEnum);
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    @Override
    public List<Veicolo> findByAnnoProduzione(Integer annoProduzioneMinimo, Integer annoProduzioneMassimo) {
        Map<String,String> errori = new TreeMap<>();
        if(annoProduzioneMinimo == null && annoProduzioneMassimo == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        if(annoProduzioneMinimo == null) {
            annoProduzioneMinimo = 1900;
        }
        if(annoProduzioneMassimo == null) {
            annoProduzioneMassimo = LocalDateTime.now().getYear();
        }
        if(annoProduzioneMinimo < 1900) {
            errori.put("annoProduzioneMinimo", "L'anno di produzione minimo non è valido");
        }
        if(annoProduzioneMassimo > LocalDateTime.now().getYear()) {
            errori.put("annoProduzioneMassimo", "L'anno di produzione massimo non è valido");
        }
        if(annoProduzioneMassimo < annoProduzioneMinimo) {
            errori.put("consistenza", "L'anno di produzione massimo non può essere prima dell'anno di produzione minimo");
        }
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByAnnoProduzioneBetween(annoProduzioneMinimo, annoProduzioneMassimo);
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }

    }

    @Override
    public List<Veicolo> findByPrezzo(Double prezzoMinimo, Double prezzoMassimo) {
        Map<String,String> errori = new TreeMap<>();
        if(prezzoMinimo == null && prezzoMassimo == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        if(prezzoMinimo == null) {
            prezzoMinimo = 0.0;
        }
        if(prezzoMassimo == null) {
            prezzoMassimo = Double.MAX_VALUE;
        }
        if(prezzoMinimo < 0.0) {
            errori.put("prezzoMinimo", "Il prezzo minimo non è valido");
        }
        if(prezzoMassimo > Double.MAX_VALUE) {
            errori.put("prezzoMassimo", "Il prezzo massimo non è valido");
        }
        if(prezzoMassimo < prezzoMinimo) {
            errori.put("consistenza", "Il prezzo massimo non può essere minore del prezzo minimo");
        }
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByPrezzoBetween(prezzoMinimo, prezzoMassimo);
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    @Override
    public List<Veicolo> findByPotenza(Integer potenzaMinima, Integer potenzaMassima) {
        Map<String,String> errori = new TreeMap<>();
        if(potenzaMinima == null && potenzaMassima == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        if(potenzaMinima == null) {
            potenzaMinima = 0;
        }
        if(potenzaMassima == null) {
            potenzaMassima = Integer.MAX_VALUE;
        }
        if(potenzaMinima < 0) {
            errori.put("potenzaMinima", "La potenza minima non è valida");
        }
        if(potenzaMassima < potenzaMinima) {
            errori.put("consistenza", "La potenza massima non può essere minore della potenza minima");
        }
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByPotenzaCvBetween(potenzaMinima, potenzaMassima);
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    @Override
    public List<Veicolo> findByChilometraggio(Integer chilometraggioMinimo, Integer chilometraggioMassimo) {
        Map<String,String> errori = new TreeMap<>();
        if(chilometraggioMinimo == null && chilometraggioMassimo == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        if(chilometraggioMinimo == null) {
            chilometraggioMinimo = 0;
        }
        if(chilometraggioMassimo == null) {
            chilometraggioMassimo = Integer.MAX_VALUE;
        }
        if(chilometraggioMinimo < 0) {
            errori.put("chilometraggioMinimo", "Il chilometraggio minimo non è valido");
        }
        if(chilometraggioMassimo < chilometraggioMinimo) {
            errori.put("consistenza", "Il chilometraggio massimo non può essere minore del chilometraggio minimo");
        }
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByChilometraggioBetween(chilometraggioMinimo, chilometraggioMassimo);
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    @Override
    public void registraVendita(Long idVeicolo, Utente acquirente) {
        Map<String,String> errori = new TreeMap<>();
        if(idVeicolo == null || idVeicolo <= 0) {
            errori.put("veicolo", "Id veicolo non valido");
        }
        if(acquirente == null) {
            errori.put("acquirente", "Acquirente non valido");
        }
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Veicolo> veicolo = veicoloRepo.findById(idVeicolo);
        if(veicolo.isEmpty()) {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        if(veicolo.get().getAcquirente() != null) {
            throw new VeicoloVendutoException("Veicolo già venduto");
        }
        veicolo.get().setAcquirente(acquirente);
        veicoloRepo.save(veicolo.get());

    }

    @Override
    public List<DettagliVeicoloManagerResponseDTO> findAllConDettagli() throws IOException {
        List<Veicolo> veicoli = veicoloRepo.findAll();
        List<DettagliVeicoloManagerResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoli) {
            byte[] immagine = null;
            if(veicolo.getFilePath() != null) {
                immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
            }
            DettagliVeicoloManagerResponseDTO dettagli = new DettagliVeicoloManagerResponseDTO();
            dettagli.setId(veicolo.getId());
            dettagli.setTarga(veicolo.getTarga());
            dettagli.setMarca(veicolo.getMarca());
            dettagli.setModello(veicolo.getModello());
            dettagli.setChilometraggio(veicolo.getChilometraggio());
            dettagli.setAnnoProduzione(veicolo.getAnnoProduzione());
            dettagli.setPotenzaCv(veicolo.getPotenzaCv());
            dettagli.setAlimentazione(veicolo.getAlimentazione());
            dettagli.setPrezzo(veicolo.getPrezzo());
            dettagli.setStato(checkStato(veicolo));
            dettagli.setImmagine(immagine);
            veicoliResponse.add(dettagli);
        }
        return veicoliResponse;
    }

    @Override
    public void eliminaVeicolo(Long idVeicolo) {
        Map<String,String> errori = new TreeMap<>();
        if(idVeicolo == null || idVeicolo <= 0) {
            errori.put("id", "Id veicolo non valido");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Veicolo> veicolo = veicoloRepo.findById(idVeicolo);
        if(veicolo.isEmpty()) {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        String stato = checkStato(veicolo.get());
        if(stato.equals("VENDUTO")){
            throw new VeicoloVendutoException("Non è possibile eliminare un veicolo venduto");
        }
        veicoloRepo.delete(veicolo.get());
    }

    @Override
    public void modificaVeicolo(Long idVeicolo, ModificaVeicoloRequestDTO request) {
        Map<String, String> errori = new TreeMap<>();
        if (idVeicolo == null || idVeicolo <= 0) {
            errori.put("id", "Id veicolo non valido");
        }
        if (request == null) {
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        if (request.getTarga() == null || request.getTarga().isEmpty() || request.getTarga().isBlank()) {
            errori.put("targa", "Devi inserire una targa valida");
        }
        if (request.getMarca() == null || request.getMarca().isEmpty() || request.getMarca().isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
        }
        if (request.getModello() == null || request.getModello().isEmpty() || request.getModello().isBlank()) {
            errori.put("modello", "Devi inserire un modello valido");
        }
        if (request.getChilometraggio() == null || request.getChilometraggio() < 0) {
            errori.put("chilometraggio", "Devi inserire un chilometraggio valido");
        }
        if (request.getAnnoProduzione() == null || request.getAnnoProduzione() < 1900 || request.getAnnoProduzione() > LocalDateTime.now().getYear()) {
            errori.put("annoProduzione", "Devi inserire un anno di produzione valido");
        }
        if (request.getPotenzaCv() == null || request.getPotenzaCv() < 0) {
            errori.put("potenzaCv", "Devi inserire una potenza valida");
        }

        if (request.getPrezzo() == null || request.getPrezzo() < 0.0) {
            errori.put("prezzo", "Devi inserire un prezzo valido");
        }

        Alimentazione alimentazione = null;

        if (request.getAlimentazione() == null || request.getAlimentazione().isEmpty() || request.getAlimentazione().isBlank()) {
            errori.put("alimentazione", "Devi inserire un tipo di alimentazione valido");
        } else {
            alimentazione = switch (request.getAlimentazione()) {
                case "BENZINA" -> Alimentazione.BENZINA;
                case "DIESEL" -> Alimentazione.DIESEL;
                case "IBRIDA" -> Alimentazione.IBRIDA;
                case "GPL" -> Alimentazione.GPL;
                case "ELETTRICA" -> Alimentazione.ELETTRICA;
                default -> throw new AlimentazioneNonValidaException("Tipo di alimentazione non valido");
            };
        }

        if (!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Veicolo> veicolo = veicoloRepo.findById(idVeicolo);
        if (veicolo.isEmpty()) {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        String stato = checkStato(veicolo.get());
        if(stato.equals("VENDUTO")){
            throw new VeicoloVendutoException("Non è possibile modificare un veicolo venduto");
        }
        veicolo.get().setTarga(request.getTarga());
        veicolo.get().setMarca(request.getMarca());
        veicolo.get().setModello(request.getModello());
        veicolo.get().setChilometraggio(request.getChilometraggio());
        veicolo.get().setAnnoProduzione(request.getAnnoProduzione());
        veicolo.get().setPotenzaCv(request.getPotenzaCv());
        veicolo.get().setAlimentazione(alimentazione);
        veicolo.get().setPrezzo(request.getPrezzo());
        veicoloRepo.save(veicolo.get());
    }

    @Override
    public List<DettagliVeicoloManagerResponseDTO> findAllDisponibili() throws IOException{
        List<Veicolo> veicoli = veicoloRepo.findAll();
        List<DettagliVeicoloManagerResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoli) {

            String stato = checkStato(veicolo);
            if(stato.equals("DISPONIBILE")){
                byte[] immagine = null;
                if(veicolo.getFilePath() != null) {
                    immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
                }
                DettagliVeicoloManagerResponseDTO dettagli = new DettagliVeicoloManagerResponseDTO();
                dettagli.setId(veicolo.getId());
                dettagli.setTarga(veicolo.getTarga());
                dettagli.setMarca(veicolo.getMarca());
                dettagli.setModello(veicolo.getModello());
                dettagli.setChilometraggio(veicolo.getChilometraggio());
                dettagli.setAnnoProduzione(veicolo.getAnnoProduzione());
                dettagli.setPotenzaCv(veicolo.getPotenzaCv());
                dettagli.setAlimentazione(veicolo.getAlimentazione());
                dettagli.setPrezzo(veicolo.getPrezzo());
                dettagli.setStato(checkStato(veicolo));
                dettagli.setImmagine(immagine);
                veicoliResponse.add(dettagli);
            }
        }
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
        }
        return veicoliResponse;
    }

    @Override
    public List<DettagliVeicoloManagerResponseDTO> findAllDisponibiliESelezionato(Long idAppuntamento) throws IOException {
        Map<String, String> errori = new TreeMap<>();
        if (idAppuntamento == null || idAppuntamento <= 0) {
            errori.put("id", "Id dell'appuntamento non valido");
            throw new CredenzialiNonValideException(errori);
        }

        List<Veicolo> veicoli = veicoloRepo.findAll();
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        if (appuntamento.isEmpty()) {
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        List<DettagliVeicoloManagerResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoli) {

            String stato = checkStato(veicolo);
            if(stato.equals("DISPONIBILE") || veicolo.getId() == appuntamento.get().getVeicolo().getId()){
                byte[] immagine = null;
                if(veicolo.getFilePath() != null) {
                    immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
                }
                DettagliVeicoloManagerResponseDTO dettagli = new DettagliVeicoloManagerResponseDTO();
                dettagli.setId(veicolo.getId());
                dettagli.setTarga(veicolo.getTarga());
                dettagli.setMarca(veicolo.getMarca());
                dettagli.setModello(veicolo.getModello());
                dettagli.setChilometraggio(veicolo.getChilometraggio());
                dettagli.setAnnoProduzione(veicolo.getAnnoProduzione());
                dettagli.setPotenzaCv(veicolo.getPotenzaCv());
                dettagli.setAlimentazione(veicolo.getAlimentazione());
                dettagli.setPrezzo(veicolo.getPrezzo());
                dettagli.setStato(checkStato(veicolo));
                dettagli.setImmagine(immagine);
                veicoliResponse.add(dettagli);
            }
        }
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
        }
        return veicoliResponse;
    }

    @Override
    public List<Long> estraiIdDaFindAllDisponibili(List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili) {
        List<Long> idVeicoli = new ArrayList<>();
        for (DettagliVeicoloManagerResponseDTO veicolo : veicoliDisponibili) {
            idVeicoli.add(veicolo.getId());
        }
        return idVeicoli;
    }

    @Override
    public DettagliVeicoloManagerResponseDTO recuperaDettagli(Long idVeicolo) throws IOException {
        Map<String,String> errori = new TreeMap<>();
        if (idVeicolo == null || idVeicolo <= 0) {
            errori.put("id", "Id veicolo non valido");
            throw new CredenzialiNonValideException(errori);
        }
        Veicolo veicolo = veicoloRepo.findById(idVeicolo).orElseThrow(() -> new VeicoloNonTrovatoException("Veicolo non trovato"));
        byte[] immagine = null;
        if(veicolo.getFilePath() != null) {
            immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
        }
        DettagliVeicoloManagerResponseDTO response = new DettagliVeicoloManagerResponseDTO();
        response.setId(veicolo.getId());
        response.setTarga(veicolo.getTarga());
        response.setMarca(veicolo.getMarca());
        response.setModello(veicolo.getModello());
        response.setChilometraggio(veicolo.getChilometraggio());
        response.setAnnoProduzione(veicolo.getAnnoProduzione());
        response.setPotenzaCv(veicolo.getPotenzaCv());
        response.setAlimentazione(veicolo.getAlimentazione());
        response.setPrezzo(veicolo.getPrezzo());
        response.setStato(checkStato(veicolo));
        response.setImmagine(immagine);
        return response;
    }

    private String checkStato(Veicolo veicolo){
        if(veicolo.getAcquirente() != null){
            veicolo.setStato(new Venduto(veicolo));
            return "VENDUTO";
        } else {
            Optional<List<Appuntamento>> appuntamenti = appuntamentoRepo.findByVeicolo_IdAndEsitoRegistratoIsFalse(veicolo.getId());
            if (appuntamenti.isPresent() && appuntamenti.get().isEmpty()) {
                veicolo.setStato(new Disponibile(veicolo));
                return "DISPONIBILE";
            } else {
                veicolo.setStato(new Trattativa(veicolo));
                return "TRATTATIVA";
            }
        }
    }

}
