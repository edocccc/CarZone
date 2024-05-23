package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.persistence.service.VeicoloService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class RicercaChilometraggioTest {

    @Mock
    VeicoloService veicoloService;

    @InjectMocks
    RicercaChilometraggio ricercaChilometraggio;

    @Test
    void ricercaSuccessful() {
        assertAll(() -> ricercaChilometraggio.ricerca(new RicercaRequestDTO()));
    }

}
