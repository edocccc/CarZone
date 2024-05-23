package com.polimi.carzone.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class CustomExceptionHandlerTest {
    @InjectMocks
    CustomExceptionHandler customExceptionHandler;

    @Test
    void gestisciCredenzialiNonValideSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciCredezialiNonValideException(new CredenzialiNonValideException(new TreeMap<>())));
    }

    @Test
    void gestisciUtenteNonTrovatoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciUtenteNonTrovatoException(new UtenteNonTrovatoException("Messaggio")));
    }

    @Test
    void sqlIntegrityConstraintViolationExceptionSuccessful() {
        assertAll(() -> customExceptionHandler.sqlIntegrityConstraintViolationException(new SQLIntegrityConstraintViolationException("Messaggio")));
    }

    @Test
    void gestisciAlimentazioneNonValidaSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciAlimentazioneNonValidaException(new AlimentazioneNonValidaException("Messaggio")));
    }

    @Test
    void gestisciVeicoliNonDisponibiliSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciVeicoliNonDisponibiliException(new VeicoliNonDisponibiliException("Messaggio")));
    }

    @Test
    void gestisciVeicoloNonTrovatoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciVeicoloNonTrovatoException(new VeicoloNonTrovatoException("Messaggio")));
    }

    @Test
    void gestisciCriterioNonValidoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciCriterioNonValidoException(new CriterioNonValidoException("Messaggio")));
    }

    @Test
    void gestisciAppuntamentoNonTrovatoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciAppuntamentoNonTrovatoException(new AppuntamentoNonTrovatoException("Messaggio")));
    }

    @Test
    void gestisciVeicoloVendutoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciVeicoloVendutoException(new VeicoloVendutoException("Messaggio")));
    }

    @Test
    void gestisciRuoloNonValidoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciRuoloNonValidoException(new RuoloNonValidoException("Messaggio")));
    }

    @Test
    void gestisciDiversiIdSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciDiversiIdException(new DiversiIdException("Messaggio")));
    }

    @Test
    void gestisciTokenNonValidoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciTokenNonValidoException(new TokenNonValidoException("Messaggio")));
    }

    @Test
    void gestisciVeicoloNonTraDisponibiliSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciVeicoloNonTraDisponibiliException(new VeicoloNonTraDisponibiliException("Messaggio")));
    }

    @Test
    void gestisciRecensioneGiaLasciataSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciRecensioneGiaLasciataException(new RecensioneGiaLasciataException("Messaggio")));
    }

    @Test
    void gestisciAppuntamentoNonSvoltoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciAppuntamentoNonSvoltoException(new AppuntamentoNonSvoltoException("Messaggio")));
    }

    @Test
    void gestisciDipendenteNonAssegnatoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciDipendenteNonAssegnatoException(new DipendenteNonAssegnatoException("Messaggio")));
    }

    @Test
    void gestisciAppuntamentoRegistratoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciAppuntamentoRegistratoException(new AppuntamentoRegistratoException("Messaggio")));
    }

    @Test
    void gestisciAppuntamentoNonAssegnatoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciAppuntamentoNonAssegnatoException(new AppuntamentoNonAssegnatoException("Messaggio")));
    }

    @Test
    void gestisciAppuntamentoPresoInCaricoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciAppuntamentoPresoInCaricoException(new AppuntamentoPresoInCaricoException("Messaggio")));
    }

    @Test
    void gestisciAppuntamentoPassatoSuccessful() {
        assertAll(() -> customExceptionHandler.gestisciAppuntamentoPassatoException(new AppuntamentoPassatoException("Messaggio")));
    }


}
