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

//annotazione per indicare a Spring che la classe è un service
@Service
//annotazione per indicare che i metodi della classe sono transazionali
//vengono eseguiti eseguiti all'interno di una transazione
@Transactional
//annotazione per generare un costruttore con un parametro per ciascun campo
@RequiredArgsConstructor
public class VeicoloServiceImpl implements VeicoloService {
    //la classe implementa i metodi dichiarati nell'interfaccia VeicoloService

    //dichiarazione della repository per la gestione dei veicoli
    private final VeicoloRepository veicoloRepo;
    //dichiarazione della repository per la gestione degli appuntamenti
    private final AppuntamentoRepository appuntamentoRepo;
    //dichiarazione del path della cartella in cui vengono salvate le immagini
    private static final String FOLDER_PATH = "C:/Users/casca/Desktop/unigenerale/IngegneriaDS/progettofinale/Progetto/CarZone/src/main/resources/immagini/";

    //metodo per il manager che a partire da una richiesta e un file immagine, aggiunge un veicolo
    @Override
    public void aggiungiVeicolo(AggiuntaVeicoloRequestDTO request, MultipartFile immagine) throws IOException {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i parametri passati
        //se la request è null, aggiungo un errore alla mappa e lancio un'eccezione
        if(request == null) {
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        //se l'immagine è null, aggiungo un errore alla mappa e lancio un'eccezione
        if(immagine == null) {
            errori.put("immagine", "Devi inserire un'immagine");
            throw new CredenzialiNonValideException(errori);
        }

        //cerco un veicolo con la targa passata
        Optional<Veicolo> checkTarga = veicoloRepo.findByTarga(request.getTarga());
        //se il veicolo è presente, aggiungo un errore alla mappa e lancio un'eccezione
        if(checkTarga.isPresent()) {
            errori.put("targa", "Targa già presente nel sistema");
            throw new CredenzialiNonValideException(errori);
        }
        //se la targa è null, vuota o composta solo da spazi, aggiungo un errore alla mappa
        if(request.getTarga() == null || request.getTarga().isEmpty() || request.getTarga().isBlank()) {
            errori.put("targa", "Devi inserire una targa valida");
        }
        //se la marca è null o vuota o composta solo da spazi, aggiungo un errore alla mappa
        if(request.getMarca() == null || request.getMarca().isEmpty() || request.getMarca().isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
        }
        //se il modello è null o vuoto o composto solo da spazi, aggiungo un errore alla mappa
        if(request.getModello() == null || request.getModello().isEmpty() || request.getModello().isBlank()) {
            errori.put("modello", "Devi inserire un modello valido");
        }
        //se il chilometraggio è null o minore di 0, aggiungo un errore alla mappa
        if(request.getChilometraggio() == null || request.getChilometraggio() < 0) {
            errori.put("chilometraggio", "Devi inserire un chilometraggio valido");
        }
        //se l'anno di produzione è null o minore di 1900 o maggiore dell'anno corrente, aggiungo un errore alla mappa
        if(request.getAnnoProduzione() == null || request.getAnnoProduzione() < 1900 || request.getAnnoProduzione() > LocalDateTime.now().getYear()) {
            errori.put("annoProduzione", "Devi inserire un anno di produzione valido");
        }
        //se la potenza è null o minore di 0, aggiungo un errore alla mappa
        if(request.getPotenzaCv() == null || request.getPotenzaCv() < 0) {
            errori.put("potenzaCv", "Devi inserire una potenza valida");
        }

        //se il prezzo è null o minore di 0, aggiungo un errore alla mappa
        if(request.getPrezzo() == null || request.getPrezzo() < 0.0) {
            errori.put("prezzo", "Devi inserire un prezzo valido");
        }

        //dichiarazione di una variabile di tipo Alimentazione
        Alimentazione alimentazione = null;

        //se l'alimentazione passata è null o vuota o composta solo da spazi, aggiungo un errore alla mappa
        if(request.getAlimentazione() == null || request.getAlimentazione().isEmpty() || request.getAlimentazione().isBlank()) {
            errori.put("alimentazione", "Devi inserire un tipo di alimentazione valido");
        } else {
            //assegno alla variabile alimentazione il valore corrispondente al tipo di alimentazione passato
            alimentazione = switch (request.getAlimentazione()) {
                //se l'alimentazione non è uno dei valori contenuti nella classe Enum Alimentazione, lancio un'eccezione
                case "BENZINA" -> Alimentazione.BENZINA;
                case "DIESEL" -> Alimentazione.DIESEL;
                case "IBRIDA" -> Alimentazione.IBRIDA;
                case "GPL" -> Alimentazione.GPL;
                case "ELETTRICA" -> Alimentazione.ELETTRICA;
                default -> throw new AlimentazioneNonValidaException("Tipo di alimentazione non valido");
            };
        }

        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }

        //se i controlli sono stati superati, creo un nuovo veicolo
        Veicolo veicolo = new Veicolo();
        //setto i campi del veicolo con i valori passati nella request
        veicolo.setTarga(request.getTarga());
        veicolo.setMarca(request.getMarca());
        veicolo.setModello(request.getModello());
        veicolo.setChilometraggio(request.getChilometraggio());
        veicolo.setAnnoProduzione(request.getAnnoProduzione());
        veicolo.setPotenzaCv(request.getPotenzaCv());
        veicolo.setAlimentazione(alimentazione);
        veicolo.setPrezzo(request.getPrezzo());
        //setto i parametri dell'immagine del veicolo
        //setto il nome dell'immagine con il nome originale dell'immagine
        veicolo.setNomeImmagine(immagine.getOriginalFilename());
        //setto il tipo dell'immagine con il tipo dell'immagine
        veicolo.setTipoImmagine(immagine.getContentType());
        //setto il path dell'immagine con il path della cartella in cui verrà salvata l'immagine aggiunto del nome dell'immagine
        veicolo.setFilePath(FOLDER_PATH + immagine.getOriginalFilename());

        //salvo il veicolo nel database
        veicoloRepo.save(veicolo);
        //creo un nuovo file con il path della cartella in cui verrà salvata l'immagine aggiunto del nome dell'immagine
        File file = new File(FOLDER_PATH + immagine.getOriginalFilename());
        //trasferisco l'immagine nel file
        immagine.transferTo(file);
    }

    //metodo che trova tutti i veicoli
    @Override
    public List<VeicoloResponseDTO> findAll() throws IOException {
        //trovo tutti i veicoli presenti nel database
        List<Veicolo> veicoli = veicoloRepo.findAll();
        //creo una lista di veicoli tramite il DTO
        List<VeicoloResponseDTO> veicoliResponse = new ArrayList<>();
        //per ogni veicolo trovato, inizializzo un array di byte per l'immagine
        for (Veicolo veicolo : veicoli) {
            byte[] immagine = null;
            //se il veicolo ha un path dell'immagine, leggo l'immagine e la assegno all'array di byte
            if(veicolo.getFilePath() != null) {
                immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
            }
            //aggiungo alla lista di veicoliResponse il veicolo trovato settando i campi con i valori del veicolo
            veicoliResponse.add(new VeicoloResponseDTO(
                    veicolo.getId(),
                    veicolo.getMarca(),
                    veicolo.getModello(),
                    veicolo.getPrezzo(),
                    //controllo lo stato del veicolo e lo setto
                    checkStato(veicolo),
                    immagine
            ));
        }
        //restituisco la lista di veicoli
        return veicoliResponse;
    }

    //metodo che a partire da una targa, trova un veicolo
    @Override
    public Veicolo findByTarga(String targa) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se la targa è null o vuota o composta solo da spazi, aggiungo un errore alla mappa e lancio un'eccezione
        if(targa == null || targa.isEmpty() || targa.isBlank()) {
            errori.put("targa", "Devi inserire una targa valida");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco un veicolo con la targa passata
        Optional<Veicolo> veicolo = veicoloRepo.findByTarga(targa);
        //se il veicolo è presente, lo restituisco
        if(veicolo.isPresent()){
            return veicolo.get();
        } else {
            //se il veicolo non è presente, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
    }

    //metodo che a partire da un id, trova un veicolo
    @Override
    public Veicolo findById(long id) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'id è minore o uguale a 0, aggiungo un errore alla mappa e lancio un'eccezione
        if(id <= 0) {
            errori.put("id", "Id veicolo non valido");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco un veicolo con l'id passato
        Optional<Veicolo> veicolo = veicoloRepo.findById(id);
        //se il veicolo è presente, lo restituisco
        if(veicolo.isPresent()){
            return veicolo.get();
        } else {
            //se il veicolo non è presente, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
    }

    //metodo che a partire da una lista di veicoli, li converte in una lista di veicoliResponse
    @Override
    public List<VeicoloResponseDTO> convertiVeicoliInVeicoliResponse(List<Veicolo> veicoliTrovati) throws IOException {
        //creo una lista di veicoli tramite il DTO
        List<VeicoloResponseDTO> veicoliResponse = new ArrayList<>();
        //per ogni veicolo trovato, inizializzo un array di byte per l'immagine
        for (Veicolo veicolo : veicoliTrovati) {
            byte[] immagine = null;
            //se il veicolo ha un path dell'immagine, leggo l'immagine e la assegno all'array di byte
            if(veicolo.getFilePath() != null) {
                immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
            }
            //aggiungo alla lista di veicoliResponse il veicolo trovato settando i campi con i valori del veicolo
            veicoliResponse.add(new VeicoloResponseDTO(
                    veicolo.getId(),
                    veicolo.getMarca(),
                    veicolo.getModello(),
                    veicolo.getPrezzo(),
                    //controllo lo stato del veicolo e lo setto
                    checkStato(veicolo),
                    immagine
            ));
        }
        //se la lista di veicoliResponse è vuota, lancio un'eccezione
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
        }
        //restituisco la lista di veicoli
        return veicoliResponse;
    }

    //metodo che a partire da una marca, trova i veicoli corrispondenti
    @Override
    public List<Veicolo> findByMarca(String marca) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se la marca è null o vuota o composta solo da spazi, aggiungo un errore alla mappa e lancio un'eccezione
        if(marca == null || marca.isEmpty() || marca.isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco i veicoli con la marca passata
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByMarca(marca);
        //se i veicoli sono presenti, li restituisco
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            //se i veicoli non sono presenti, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    //metodo che a partire da una marca e un modello, trova i veicoli corrispondenti
    @Override
    public List<Veicolo> findByMarcaAndModello(String marca,String modello) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se la marca è null o vuota o composta solo da spazi, aggiungo un errore alla mappa
        if(marca == null || marca.isEmpty() || marca.isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
        }
        //se il modello è null o vuoto o composto solo da spazi, aggiungo un errore alla mappa
        if(modello == null || modello.isEmpty() || modello.isBlank()) {
            errori.put("modello", "Devi inserire un modello valido");
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        //cerco i veicoli con la marca e il modello passati
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByMarcaAndModello(marca,modello);
        //se i veicoli sono presenti, li restituisco
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            //se i veicoli non sono presenti, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    //metodo che a partire da un tipo di alimentazione, trova i veicoli corrispondenti
    @Override
    public List<Veicolo> findByAlimentazione(String alimentazione) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'alimentazione è null o vuota o composta solo da spazi, aggiungo un errore alla mappa
        if(alimentazione == null || alimentazione.isEmpty() || alimentazione.isBlank()) {
            errori.put("alimentazione", "Devi inserire una alimentazione valida");
            throw new CredenzialiNonValideException(errori);
        }
        //assegno alla variabile alimentazioneEnum il valore corrispondente al tipo di alimentazione passato
        Alimentazione alimentazioneEnum = switch (alimentazione) {
            //se l'alimentazione non è uno dei valori contenuti nella classe Enum Alimentazione, lancio un'eccezione
            case "BENZINA" -> Alimentazione.BENZINA;
            case "DIESEL" -> Alimentazione.DIESEL;
            case "IBRIDA" -> Alimentazione.IBRIDA;
            case "GPL" -> Alimentazione.GPL;
            case "ELETTRICA" -> Alimentazione.ELETTRICA;
            default -> throw new AlimentazioneNonValidaException("Tipo di alimentazione non valido");
        };
        //cerco i veicoli con il tipo di alimentazione passato
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByAlimentazione(alimentazioneEnum);
        //se i veicoli sono presenti, li restituisco
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            //se i veicoli non sono presenti, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    //metodo che a partire da un anno di produzione minimo e uno massimo, trova i veicoli corrispondenti
    @Override
    public List<Veicolo> findByAnnoProduzione(Integer annoProduzioneMinimo, Integer annoProduzioneMassimo) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'anno di produzione minimo e massimo sono null, aggiungo un errore alla mappa
        if(annoProduzioneMinimo == null && annoProduzioneMassimo == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        //se l'anno di produzione minimo è null, lo setto a 1900
        if(annoProduzioneMinimo == null) {
            annoProduzioneMinimo = 1900;
        }
        //se l'anno di produzione massimo è null, lo setto all'anno corrente
        if(annoProduzioneMassimo == null) {
            annoProduzioneMassimo = LocalDateTime.now().getYear();
        }
        //se l'anno di produzione minimo è minore di 1900, aggiungo un errore alla mappa
        if(annoProduzioneMinimo < 1900) {
            errori.put("annoProduzioneMinimo", "L'anno di produzione minimo non è valido");
        }
        //se l'anno di produzione massimo è maggiore dell'anno corrente, aggiungo un errore alla mappa
        if(annoProduzioneMassimo > LocalDateTime.now().getYear()) {
            errori.put("annoProduzioneMassimo", "L'anno di produzione massimo non è valido");
        }
        //se l'anno di produzione massimo è minore dell'anno di produzione minimo, aggiungo un errore alla mappa
        if(annoProduzioneMassimo < annoProduzioneMinimo) {
            errori.put("consistenza", "L'anno di produzione massimo non può essere prima dell'anno di produzione minimo");
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        //cerco i veicoli con l'anno di produzione compreso tra l'anno minimo e massimo passati
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByAnnoProduzioneBetween(annoProduzioneMinimo, annoProduzioneMassimo);
        //se i veicoli sono presenti, li restituisco
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            //se i veicoli non sono presenti, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }

    }

    //metodo che a partire da un prezzo minimo e uno massimo, trova i veicoli corrispondenti
    @Override
    public List<Veicolo> findByPrezzo(Double prezzoMinimo, Double prezzoMassimo) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se il prezzo minimo e massimo sono null, aggiungo un errore alla mappa
        if(prezzoMinimo == null && prezzoMassimo == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        //se il prezzo minimo è null, lo setto a 0.0
        if(prezzoMinimo == null) {
            prezzoMinimo = 0.0;
        }
        //se il prezzo massimo è null, lo setto al massimo valore di un double
        if(prezzoMassimo == null) {
            prezzoMassimo = Double.MAX_VALUE;
        }
        //se il prezzo minimo è minore di 0, aggiungo un errore alla mappa
        if(prezzoMinimo < 0.0) {
            errori.put("prezzoMinimo", "Il prezzo minimo non è valido");
        }
        //se il prezzo massimo è maggiore del massimo valore di un double, aggiungo un errore alla mappa
        if(prezzoMassimo > Double.MAX_VALUE) {
            errori.put("prezzoMassimo", "Il prezzo massimo non è valido");
        }
        //se il prezzo massimo è minore del prezzo minimo, aggiungo un errore alla mappa
        if(prezzoMassimo < prezzoMinimo) {
            errori.put("consistenza", "Il prezzo massimo non può essere minore del prezzo minimo");
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        //cerco i veicoli con il prezzo compreso tra il prezzo minimo e massimo passati
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByPrezzoBetween(prezzoMinimo, prezzoMassimo);
        //se i veicoli sono presenti, li restituisco
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            //se i veicoli non sono presenti, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    //metodo che a partire da una potenza minima e una massima, trova i veicoli corrispondenti
    @Override
    public List<Veicolo> findByPotenza(Integer potenzaMinima, Integer potenzaMassima) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se la potenza minima e massima sono null, aggiungo un errore alla mappa
        if(potenzaMinima == null && potenzaMassima == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        //se la potenza minima è null, la setto a 0
        if(potenzaMinima == null) {
            potenzaMinima = 0;
        }
        //se la potenza massima è null, la setto al massimo valore di un intero
        if(potenzaMassima == null) {
            potenzaMassima = Integer.MAX_VALUE;
        }
        //se la potenza minima è minore di 0, aggiungo un errore alla mappa
        if(potenzaMinima < 0) {
            errori.put("potenzaMinima", "La potenza minima non è valida");
        }
        //se la potenza massima è minore della potenza minima, aggiungo un errore alla mappa
        if(potenzaMassima < potenzaMinima) {
            errori.put("consistenza", "La potenza massima non può essere minore della potenza minima");
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        //cerco i veicoli con la potenza compresa tra la potenza minima e massima passate
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByPotenzaCvBetween(potenzaMinima, potenzaMassima);
        //se i veicoli sono presenti, li restituisco
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            //se i veicoli non sono presenti, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    //metodo che a partire da un chilometraggio minimo e uno massimo, trova i veicoli corrispondenti
    @Override
    public List<Veicolo> findByChilometraggio(Integer chilometraggioMinimo, Integer chilometraggioMassimo) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se il chilometraggio minimo e massimo sono null, aggiungo un errore alla mappa
        if(chilometraggioMinimo == null && chilometraggioMassimo == null) {
            errori.put("consistenza", "Devi inserire almeno un parametro per ricerca");
        }
        //se il chilometraggio minimo è null, lo setto a 0
        if(chilometraggioMinimo == null) {
            chilometraggioMinimo = 0;
        }
        //se il chilometraggio massimo è null, lo setto al massimo valore di un intero
        if(chilometraggioMassimo == null) {
            chilometraggioMassimo = Integer.MAX_VALUE;
        }
        //se il chilometraggio minimo è minore di 0, aggiungo un errore alla mappa
        if(chilometraggioMinimo < 0) {
            errori.put("chilometraggioMinimo", "Il chilometraggio minimo non è valido");
        }
        //se il chilometraggio massimo è minore del chilometraggio minimo, aggiungo un errore alla mappa
        if(chilometraggioMassimo < chilometraggioMinimo) {
            errori.put("consistenza", "Il chilometraggio massimo non può essere minore del chilometraggio minimo");
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        //cerco i veicoli con il chilometraggio compreso tra il chilometraggio minimo e massimo passati
        Optional<List<Veicolo>> veicoli = veicoloRepo.findByChilometraggioBetween(chilometraggioMinimo, chilometraggioMassimo);
        //se i veicoli sono presenti, li restituisco
        if(veicoli.isPresent() && !veicoli.get().isEmpty()){
            return veicoli.get();
        } else {
            //se i veicoli non sono presenti, lancio un'eccezione
            throw new VeicoloNonTrovatoException("Nessun veicolo trovato");
        }
    }

    //metodo che a partire da un id di un veicolo e un acquirente, registra la vendita del veicolo
    @Override
    public void registraVendita(Long idVeicolo, Utente acquirente) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'id del veicolo è null o minore o uguale a 0, aggiungo un errore alla mappa
        if(idVeicolo == null || idVeicolo <= 0) {
            errori.put("veicolo", "Id veicolo non valido");
        }
        //se l'acquirente è null, aggiungo un errore alla mappa
        if(acquirente == null) {
            errori.put("acquirente", "Acquirente non valido");
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        //cerco il veicolo con l'id passato
        Optional<Veicolo> veicolo = veicoloRepo.findById(idVeicolo);
        //se il veicolo non è presente, lancio un'eccezione
        if(veicolo.isEmpty()) {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        //se il veicolo è già stato venduto, lancio un'eccezione
        if(veicolo.get().getAcquirente() != null) {
            throw new VeicoloVendutoException("Veicolo già venduto");
        }
        //setto l'acquirente del veicolo con l'acquirente passato
        veicolo.get().setAcquirente(acquirente);
        //se i controlli sono stati superati, salvo il veicolo nel database
        veicoloRepo.save(veicolo.get());

    }

    //metodo per il manager che trova tutti i veicoli con i dettagli
    @Override
    public List<DettagliVeicoloManagerResponseDTO> findAllConDettagli() throws IOException {
        //trovo tutti i veicoli presenti nel database
        List<Veicolo> veicoli = veicoloRepo.findAll();
        //creo una lista di veicoli tramite il DTO
        List<DettagliVeicoloManagerResponseDTO> veicoliResponse = new ArrayList<>();
        //per ogni veicolo trovato, inizializzo un array di byte per l'immagine
        for (Veicolo veicolo : veicoli) {
            byte[] immagine = null;
            //se il veicolo ha un path dell'immagine, leggo l'immagine e la assegno all'array di byte
            if(veicolo.getFilePath() != null) {
                immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
            }
            //aggiungo alla lista di veicoliResponse il veicolo trovato settando i campi con i valori del veicolo
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
            //aggiungo alla lista di veicoliResponse il veicolo trovato
            veicoliResponse.add(dettagli);
        }
        //se la lista di veicoliResponse è vuota, lancio un'eccezione
        return veicoliResponse;
    }

    //metodo per il manager che a partire da un id, elimina un veicolo
    @Override
    public void eliminaVeicolo(Long idVeicolo) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'id del veicolo è null o minore o uguale a 0, aggiungo un errore alla mappa e lancio un'eccezione
        if(idVeicolo == null || idVeicolo <= 0) {
            errori.put("id", "Id veicolo non valido");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco il veicolo con l'id passato
        Optional<Veicolo> veicolo = veicoloRepo.findById(idVeicolo);
        //se il veicolo non è presente, lancio un'eccezione
        if(veicolo.isEmpty()) {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        //recupero lo stato del veicolo
        String stato = checkStato(veicolo.get());
        //se lo stato del veicolo è VENDUTO, lancio un'eccezione
        if(stato.equals("VENDUTO")){
            throw new VeicoloVendutoException("Non è possibile eliminare un veicolo venduto");
        }
        //se i controlli sono stati superati, elimino il veicolo
        veicoloRepo.delete(veicolo.get());
    }

    //metodo per il manager che a partire da un id e una richiesta, modifica un veicolo
    @Override
    public void modificaVeicolo(Long idVeicolo, ModificaVeicoloRequestDTO request) {
        //dichiarazione di una mappa per gli errori
        Map<String, String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'id del veicolo è null o minore o uguale a 0, aggiungo un errore alla mappa
        if (idVeicolo == null || idVeicolo <= 0) {
            errori.put("id", "Id veicolo non valido");
        }
        //se la richiesta è null, aggiungo un errore alla mappa e lancio un'eccezione
        if (request == null) {
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        //se la targa è null o vuota o composta solo da spazi, aggiungo un errore alla mappa
        if (request.getTarga() == null || request.getTarga().isEmpty() || request.getTarga().isBlank()) {
            errori.put("targa", "Devi inserire una targa valida");
        }
        //se la marca è null o vuota o composta solo da spazi, aggiungo un errore alla mappa
        if (request.getMarca() == null || request.getMarca().isEmpty() || request.getMarca().isBlank()) {
            errori.put("marca", "Devi inserire una marca valida");
        }
        //se il modello è null o vuoto o composto solo da spazi, aggiungo un errore alla mappa
        if (request.getModello() == null || request.getModello().isEmpty() || request.getModello().isBlank()) {
            errori.put("modello", "Devi inserire un modello valido");
        }
        //se il chilometraggio è null o minore di 0, aggiungo un errore alla mappa
        if (request.getChilometraggio() == null || request.getChilometraggio() < 0) {
            errori.put("chilometraggio", "Devi inserire un chilometraggio valido");
        }
        //se l'anno di produzione è null o minore di 1900 o maggiore dell'anno corrente, aggiungo un errore alla mappa
        if (request.getAnnoProduzione() == null || request.getAnnoProduzione() < 1900 || request.getAnnoProduzione() > LocalDateTime.now().getYear()) {
            errori.put("annoProduzione", "Devi inserire un anno di produzione valido");
        }
        //se la potenza è null o minore di 0, aggiungo un errore alla mappa
        if (request.getPotenzaCv() == null || request.getPotenzaCv() < 0) {
            errori.put("potenzaCv", "Devi inserire una potenza valida");
        }

        //se il prezzo è null o minore di 0, aggiungo un errore alla mappa
        if (request.getPrezzo() == null || request.getPrezzo() < 0.0) {
            errori.put("prezzo", "Devi inserire un prezzo valido");
        }

        //difinisco una variabile di tipo Alimentazione
        Alimentazione alimentazione = null;

        //se il tipo di alimentazione è null o vuoto o composto solo da spazi, aggiungo un errore alla mappa
        if (request.getAlimentazione() == null || request.getAlimentazione().isEmpty() || request.getAlimentazione().isBlank()) {
            errori.put("alimentazione", "Devi inserire un tipo di alimentazione valido");
        } else {
            //assegno alla variabile alimentazione il valore corrispondente al tipo di alimentazione passato
            alimentazione = switch (request.getAlimentazione()) {
                //se il tipo di alimentazione non è uno dei valori contenuti nella classe Enum Alimentazione, lancio un'eccezione
                case "BENZINA" -> Alimentazione.BENZINA;
                case "DIESEL" -> Alimentazione.DIESEL;
                case "IBRIDA" -> Alimentazione.IBRIDA;
                case "GPL" -> Alimentazione.GPL;
                case "ELETTRICA" -> Alimentazione.ELETTRICA;
                default -> throw new AlimentazioneNonValidaException("Tipo di alimentazione non valido");
            };
        }

        //se la mappa degli errori non è vuota, lancio un'eccezione
        if (!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }

        //cerco il veicolo con l'id passato
        Optional<Veicolo> veicolo = veicoloRepo.findById(idVeicolo);
        //se il veicolo non è presente, lancio un'eccezione
        if (veicolo.isEmpty()) {
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        //recupero lo stato del veicolo
        String stato = checkStato(veicolo.get());
        //se lo stato del veicolo è VENDUTO, lancio un'eccezione
        if(stato.equals("VENDUTO")){
            throw new VeicoloVendutoException("Non è possibile modificare un veicolo venduto");
        }
        //setto i campi del veicolo con i valori passati
        veicolo.get().setTarga(request.getTarga());
        veicolo.get().setMarca(request.getMarca());
        veicolo.get().setModello(request.getModello());
        veicolo.get().setChilometraggio(request.getChilometraggio());
        veicolo.get().setAnnoProduzione(request.getAnnoProduzione());
        veicolo.get().setPotenzaCv(request.getPotenzaCv());
        veicolo.get().setAlimentazione(alimentazione);
        veicolo.get().setPrezzo(request.getPrezzo());
        //se i controlli sono stati superati, salvo il veicolo nel database
        veicoloRepo.save(veicolo.get());
    }

    //metodo per il manager che trova tutti i veicoli disponibili
    @Override
    public List<DettagliVeicoloManagerResponseDTO> findAllDisponibili() throws IOException{
        //trovo tutti i veicoli presenti nel database
        List<Veicolo> veicoli = veicoloRepo.findAll();
        //creo una lista di veicoli tramite il DTO
        List<DettagliVeicoloManagerResponseDTO> veicoliResponse = new ArrayList<>();
        //per ogni veicolo trovato, ne recupero lo stato
        for (Veicolo veicolo : veicoli) {

            String stato = checkStato(veicolo);
            //se lo stato del veicolo è DISPONIBILE, inizializzo un array di byte per l'immagine
            if(stato.equals("DISPONIBILE")){
                byte[] immagine = null;
                //se il veicolo ha un path dell'immagine, leggo l'immagine e la assegno all'array di byte
                if(veicolo.getFilePath() != null) {
                    immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
                }
                //aggiungo alla lista di veicoliResponse il veicolo trovato settando i campi con i valori del veicolo
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
                //aggiungo alla lista di veicoliResponse il veicolo trovato
                veicoliResponse.add(dettagli);
            }
        }
        //se la lista di veicoliResponse è vuota, lancio un'eccezione
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
        }
        //se la lista di veicoliResponse non è vuota, la restituisco
        return veicoliResponse;
    }

    //metodo per il manager che trova tutti i veicoli disponibili e anche il veicolo selezionato
    @Override
    public List<DettagliVeicoloManagerResponseDTO> findAllDisponibiliESelezionato(Long idAppuntamento) throws IOException {
        //dichiarazione di una mappa per gli errori
        Map<String, String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'id dell'appuntamento è null o minore o uguale a 0, aggiungo un errore alla mappa
        if (idAppuntamento == null || idAppuntamento <= 0) {
            errori.put("id", "Id dell'appuntamento non valido");
            throw new CredenzialiNonValideException(errori);
        }

        //trovo tutti i veicoli presenti nel database
        List<Veicolo> veicoli = veicoloRepo.findAll();
        //cerco l'appuntamento con l'id passato
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        //se l'appuntamento non è presente, lancio un'eccezione
        if (appuntamento.isEmpty()) {
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        //creo una lista di veicoli tramite il DTO
        List<DettagliVeicoloManagerResponseDTO> veicoliResponse = new ArrayList<>();
        //per ogni veicolo trovato, ne recupero lo stato
        for (Veicolo veicolo : veicoli) {

            String stato = checkStato(veicolo);
            //se lo stato del veicolo è DISPONIBILE o l'id del veicolo è uguale all'id del veicolo dell'appuntamento, inizializzo un array di byte per l'immagine
            if(stato.equals("DISPONIBILE") || veicolo.getId() == appuntamento.get().getVeicolo().getId()){
                byte[] immagine = null;
                //se il veicolo ha un path dell'immagine, leggo l'immagine e la assegno all'array di byte
                if(veicolo.getFilePath() != null) {
                    immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
                }
                //aggiungo alla lista di veicoliResponse il veicolo trovato settando i campi con i valori del veicolo
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
                //aggiungo alla lista di veicoliResponse il veicolo trovato
                veicoliResponse.add(dettagli);
            }
        }
        //se la lista di veicoliResponse è vuota, lancio un'eccezione
        if(veicoliResponse.isEmpty()) {
            throw new VeicoliNonDisponibiliException("Nessun veicolo disponibile");
        }
        //se la lista di veicoliResponse non è vuota, la restituisco
        return veicoliResponse;
    }

    //metodo per il manager che a partire da una lista di veicoli disponibili, estrae gli id
    @Override
    public List<Long> estraiIdDaFindAllDisponibili(List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili) {
        //creo una lista di id di veicoli
        List<Long> idVeicoli = new ArrayList<>();
        //per ogni veicolo disponibile, aggiungo l'id alla lista di idVeicoli
        for (DettagliVeicoloManagerResponseDTO veicolo : veicoliDisponibili) {
            idVeicoli.add(veicolo.getId());
        }
        //ritorno la lista di idVeicoli
        return idVeicoli;
    }

    //metodo per il manager che a partire da un id, trova i dettagli di un veicolo
    @Override
    public DettagliVeicoloManagerResponseDTO recuperaDettagli(Long idVeicolo) throws IOException {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i dati passati
        //se l'id del veicolo è null o minore o uguale a 0, aggiungo un errore alla mappa e lancio un'eccezione
        if (idVeicolo == null || idVeicolo <= 0) {
            errori.put("id", "Id veicolo non valido");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco il veicolo con l'id passato e se non è presente, lancio un'eccezione
        Veicolo veicolo = veicoloRepo.findById(idVeicolo).orElseThrow(() -> new VeicoloNonTrovatoException("Veicolo non trovato"));
        //inizializzo un array di byte per l'immagine
        byte[] immagine = null;
        //se il veicolo ha un path dell'immagine, leggo l'immagine e la assegno all'array di byte
        if(veicolo.getFilePath() != null) {
            immagine = Files.readAllBytes(new File(veicolo.getFilePath()).toPath());
        }
        //creo la response tramite il DTO e setto i campi con i valori del veicolo
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
        //ritorno la response
        return response;
    }

    //metodo interno alla classe che a partire da un veicolo, ne recupera lo stato
    private String checkStato(Veicolo veicolo){
        //se il veicolo ha un acquirente, lo stato è VENDUTO
        if(veicolo.getAcquirente() != null){
            veicolo.setStato(new Venduto(veicolo));
            return "VENDUTO";
        } else {
            //cerco gli appuntamenti del veicolo con l'esito non registrato tramite l'id del veicolo
            Optional<List<Appuntamento>> appuntamenti = appuntamentoRepo.findByVeicolo_IdAndEsitoRegistratoIsFalse(veicolo.getId());
            //se non vengono trovati appuntamenti per il veicolo, lo stato è DISPONIBILE
            if (appuntamenti.isPresent() && appuntamenti.get().isEmpty()) {
                veicolo.setStato(new Disponibile(veicolo));
                return "DISPONIBILE";
            } else {
                //se vengono trovati appuntamenti per il veicolo, lo stato è TRATTATIVA
                veicolo.setStato(new Trattativa(veicolo));
                return "TRATTATIVA";
            }
        }
    }

}
