package com.polimi.carzone.exception;

import com.polimi.carzone.dto.response.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

// annotazione che permette di gestire le eccezioni lanciate dai controller
@RestControllerAdvice
public class CustomExceptionHandler {

    // metodo che gestisce l'eccezione CredenzialiNonValideException
    @ExceptionHandler(CredenzialiNonValideException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciCredezialiNonValideException(CredenzialiNonValideException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio dei parametri da tornare nella response
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(e.getErrori());
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione UtenteNonTrovatoException
    @ExceptionHandler(UtenteNonTrovatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciUtenteNonTrovatoException(UtenteNonTrovatoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("utente", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // metodo che gestisce l'eccezione sqlIntegrityConstraintViolationException
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("sql", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // metodo che gestisce l'eccezione AlimentazioneNonValidaException
    @ExceptionHandler(AlimentazioneNonValidaException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAlimentazioneNonValidaException(AlimentazioneNonValidaException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("alimentazione", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione VeicoliNonDisponibiliException
    @ExceptionHandler(VeicoliNonDisponibiliException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciVeicoliNonDisponibiliException(VeicoliNonDisponibiliException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("veicoli", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // metodo che gestisce l'eccezione VeicoloNonTrovatoException
    @ExceptionHandler(VeicoloNonTrovatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciVeicoloNonTrovatoException(VeicoloNonTrovatoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("veicolo", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // metodo che gestisce l'eccezione CriterioNonValidoException
    @ExceptionHandler(CriterioNonValidoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciCriterioNonValidoException(CriterioNonValidoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("criterio", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione AppuntamentoNonTrovatoException
    @ExceptionHandler(AppuntamentoNonTrovatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAppuntamentoNonTrovatoException (AppuntamentoNonTrovatoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("appuntamento", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // metodo che gestisce l'eccezione VeicoloVendutoException
    @ExceptionHandler(VeicoloVendutoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciVeicoloVendutoException (VeicoloVendutoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("veicolo", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione RuoloNonValidoException
    @ExceptionHandler(RuoloNonValidoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciRuoloNonValidoException(RuoloNonValidoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("ruolo", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione DiversiIdException
    @ExceptionHandler(DiversiIdException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciDiversiIdException(DiversiIdException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("diversiId", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione TokenNonValidoException
    @ExceptionHandler(TokenNonValidoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciTokenNonValidoException(TokenNonValidoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("token", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione VeicoloNonTraDisponibiliException
    @ExceptionHandler(VeicoloNonTraDisponibiliException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciVeicoloNonTraDisponibiliException(VeicoloNonTraDisponibiliException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("disponibilità", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione RecensioneGiaLasciataException
    @ExceptionHandler(RecensioneGiaLasciataException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciRecensioneGiaLasciataException(RecensioneGiaLasciataException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("recensione", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione AppuntamentoNonSvoltoException
    @ExceptionHandler(AppuntamentoNonSvoltoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAppuntamentoNonSvoltoException(AppuntamentoNonSvoltoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("appuntamento", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione DipendenteNonAssegnatoException
    @ExceptionHandler(DipendenteNonAssegnatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciDipendenteNonAssegnatoException(DipendenteNonAssegnatoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("dipendente", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione AppuntamentoRegistratoException
    @ExceptionHandler(AppuntamentoRegistratoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAppuntamentoRegistratoException(AppuntamentoRegistratoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("appuntamento", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione AppuntamentoNonAssegnatoException
    @ExceptionHandler(AppuntamentoNonAssegnatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAppuntamentoNonAssegnatoException(AppuntamentoNonAssegnatoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("appuntamento", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione AppuntamentoPresoInCaricoException
    @ExceptionHandler(AppuntamentoPresoInCaricoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAppuntamentoPresoInCaricoException(AppuntamentoPresoInCaricoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("appuntamento", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // metodo che gestisce l'eccezione AppuntamentoPassatoException
    @ExceptionHandler(AppuntamentoPassatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAppuntamentoPassatoException(AppuntamentoPassatoException e) {
        // creazione della response tramite il DTO
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        // settaggio degli errori da tornare nella response e del timestamp
        Map<String, String> errori = new TreeMap<>();
        errori.put("appuntamento", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        // ritorno della response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
