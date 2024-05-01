package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.exception.*;
import com.polimi.carzone.model.Alimentazione;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.state.implementation.Disponibile;
import com.polimi.carzone.state.implementation.Trattativa;
import com.polimi.carzone.state.implementation.Venduto;
import com.polimi.carzone.strategy.RicercaStrategy;
import com.polimi.carzone.strategy.implementation.RicercaAlimentazione;
import com.polimi.carzone.strategy.implementation.RicercaMarca;
import com.polimi.carzone.strategy.implementation.RicercaMarcaAndModello;
import com.polimi.carzone.strategy.implementation.RicercaTarga;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class VeicoloServiceImpl implements VeicoloService {

    private final VeicoloRepository veicoloRepo;


    @Override
    public boolean aggiungiVeicolo(AggiuntaVeicoloRequestDTO request) {
        Map<String,String> errori = new TreeMap<>();
        if(request == null) {
            errori.put("request", "La request non può essere null");
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
        if(request.getChilometraggio() < 0) {
            errori.put("chilometraggio", "Devi inserire un chilometraggio valido");
        }
        if(request.getAnnoProduzione() < 1900 || request.getAnnoProduzione() > LocalDateTime.now().getYear()) {
            errori.put("annoProduzione", "Devi inserire un anno di produzione valido");
        }
        if(request.getPotenzaCv() < 0) {
            errori.put("potenzaCv", "Devi inserire una potenza valida");
        }

        if(request.getPrezzo() < 0.0) {
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

        veicoloRepo.save(veicolo);
        return true;
    }

    @Override
    public List<VeicoloResponseDTO> findAll() {
        List<Veicolo> veicoli = veicoloRepo.findAll();
        List<VeicoloResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoli) {
            veicoliResponse.add(new VeicoloResponseDTO(
                    veicolo.getId(),
                    veicolo.getMarca(),
                    veicolo.getModello(),
                    veicolo.getPrezzo(),
                    checkStato(veicolo)
            ));
        }
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
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
    public List<VeicoloResponseDTO> convertiVeicoliInVeicoliResponse(List<Veicolo> veicoliTrovati) {
        List<VeicoloResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoliTrovati) {
            veicoliResponse.add(new VeicoloResponseDTO(
                    veicolo.getId(),
                    veicolo.getMarca(),
                    veicolo.getModello(),
                    veicolo.getPrezzo(),
                    checkStato(veicolo)
            ));
        }
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
        }
        return veicoliResponse;
    }

    @Override
    public RicercaStrategy scegliRicerca(String criterio) {
        if(criterio == null || criterio.isEmpty() || criterio.isBlank()) {
            throw new CriterioNonValidoException("Inserisci un criterio di ricerca valido");
        }
        RicercaStrategy ricercaStrategy = switch (criterio) {
            case "targa" -> new RicercaTarga(this);
            case "marca" -> new RicercaMarca(this);
            case "marcamodello" -> new RicercaMarcaAndModello(this);
            case "alimentazione" -> new RicercaAlimentazione(this);
            /*case "annoImmatricolazione" -> new RicercaAnnoImmatricolazione(veicoloRepo);
            case "prezzo" -> new RicercaPrezzo(veicoloRepo);
            case "potenza" -> new RicercaPotenza(veicoloRepo);
            case "chilometraggio" -> new RicercaChilometraggio(veicoloRepo);
            case "annoProduzione" -> new RicercaAnnoProduzione(veicoloRepo);*/
            default -> throw new CriterioNonValidoException("Criterio di ricerca non valido");
        };
        return ricercaStrategy;
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
    public DettagliVeicoloResponseDTO recuperaDettagli(long idVeicolo) {
        Map<String,String> errori = new TreeMap<>();
        if (idVeicolo <= 0) {
            errori.put("id", "Id veicolo non valido");
            throw new CredenzialiNonValideException(errori);
        }
        Veicolo veicolo = veicoloRepo.findById(idVeicolo).orElseThrow(() -> new VeicoloNonTrovatoException("Veicolo non trovato"));
        DettagliVeicoloResponseDTO response = new DettagliVeicoloResponseDTO();
        response.setId(veicolo.getId());
        response.setTarga(veicolo.getTarga());
        response.setMarca(veicolo.getMarca());
        response.setModello(veicolo.getModello());
        response.setChilometraggio(veicolo.getChilometraggio());
        response.setAnnoProduzione(veicolo.getAnnoProduzione());
        response.setPotenzaCv(veicolo.getPotenzaCv());
        response.setAlimentazione(veicolo.getAlimentazione());
        response.setPrezzo(veicolo.getPrezzo());
        return response;
    }

    private String checkStato(Veicolo veicolo){
        if(veicolo.getAcquirente() == null){
            if(veicolo.getAppuntamentiVeicolo().isEmpty()){
                veicolo.setStato(new Disponibile(veicolo));
                return "DISPONIBILE";
            } else {
                veicolo.setStato(new Trattativa(veicolo));
                return "TRATTATIVA";
            }
        } else {
            veicolo.setStato(new Venduto(veicolo));
            return "VENDUTO";
        }
    }
}
