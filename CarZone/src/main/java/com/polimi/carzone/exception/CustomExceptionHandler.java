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

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CredenzialiNonValideException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciCredezialiNonValideException(CredenzialiNonValideException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(e.getErrori());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UtenteNonTrovatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciUtenteNonTrovatoException(UtenteNonTrovatoException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        Map<String, String> errori = new TreeMap<>();
        errori.put("utente", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        Map<String, String> errori = new TreeMap<>();
        errori.put("utente", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AlimentazioneNonValidaException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAlimentazioneNonValidaException(AlimentazioneNonValidaException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        Map<String, String> errori = new TreeMap<>();
        errori.put("alimentazione", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(VeicoliNonDisponibiliException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciVeicoliNonDisponibiliException(VeicoliNonDisponibiliException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        Map<String, String> errori = new TreeMap<>();
        errori.put("veicoli", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(VeicoloNonTrovatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciVeicoloNonTrovatoException(VeicoloNonTrovatoException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        Map<String, String> errori = new TreeMap<>();
        errori.put("veicolo", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CriterioNonValidoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciCriterioNonValidoException(CriterioNonValidoException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        Map<String, String> errori = new TreeMap<>();
        errori.put("criterio", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AppuntamentoNonTrovatoException.class)
    public ResponseEntity<ExceptionResponseDTO> gestisciAppuntamentoNonTrovatoException (AppuntamentoNonTrovatoException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        Map<String, String> errori = new TreeMap<>();
        errori.put("appuntamento", e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setErrori(errori);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
