package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.persistence.service.VeicoloService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class RicercaAlimentazioneTest {

    @Mock
    VeicoloService veicoloService;

    @InjectMocks
    RicercaAlimentazione ricercaAlimentazione;

    @Test
    void ricercaSuccessful() {
        assertAll(() -> ricercaAlimentazione.ricerca(new RicercaRequestDTO()));
    }
}
