package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RicercaTargaTest {
    @Mock
    VeicoloService veicoloService;

    @InjectMocks
    RicercaTarga ricercaTarga;

    @Test
    void ricercaSuccessful() {
        Veicolo veicolo = new Veicolo();
        when(veicoloService.findByTarga(any())).thenReturn(veicolo);
        assertAll(() -> ricercaTarga.ricerca(new RicercaRequestDTO()));
    }
}
