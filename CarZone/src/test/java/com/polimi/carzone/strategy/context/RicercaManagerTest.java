package com.polimi.carzone.strategy.context;

import com.polimi.carzone.exception.CriterioNonValidoException;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RicercaManagerTest {
    @Mock
    RicercaStrategy ricercaStrategy;

    @InjectMocks
    RicercaManager ricercaManager;

    @Test
    void scegliRicercaTargaSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("targa"));
    }

    @Test
    void scegliRicercaMarcaSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("marca"));
    }

    @Test
    void scegliRicercaMarcaAndModelloSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("marcamodello"));
    }

    @Test
    void scegliRicercaAlimentazioneSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("alimentazione"));
    }

    @Test
    void scegliRicercaAnnoProduzioneSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("annoProduzione"));
    }

    @Test
    void scegliRicercaPrezzoSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("prezzo"));
    }

    @Test
    void scegliRicercaPotenzaSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("potenza"));
    }

    @Test
    void scegliRicercaChilometraggioSuccessful() {
        assertAll(() -> ricercaManager.scegliRicerca("chilometraggio"));
    }

    @Test
    void scegliRicercaThrowsCriterioNull() {
        assertThrows(CriterioNonValidoException.class,
                () -> ricercaManager.scegliRicerca(null));
    }

    @Test
    void scegliRicercaThrowsCriterioNonValido() {
        assertThrows(CriterioNonValidoException.class,
                () -> ricercaManager.scegliRicerca("CIAO"));
    }

    @Test
    void executeRicercaSuccessful() {
        assertAll(() -> ricercaManager.executeRicerca(ricercaStrategy, null));
    }
}
