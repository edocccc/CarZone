package com.polimi.carzone.state.implementation;

import com.polimi.carzone.model.Veicolo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class DisponibileTest {
    @InjectMocks
    private Disponibile disponibile;

    @Test
    void onDisponibileSuccessful() {
        Veicolo veicolo = new Veicolo();
        disponibile = new Disponibile(veicolo);
        assertAll(() -> disponibile.onDisponibile());
    }

    @Test
    void onVendutoSuccessful() {
        Veicolo veicolo = new Veicolo();
        disponibile = new Disponibile(veicolo);
        assertAll(() -> disponibile.onVenduto());
    }

    @Test
    void onTrattativaSuccessful() {
        Veicolo veicolo = new Veicolo();
        disponibile = new Disponibile(veicolo);
        assertAll(() -> disponibile.onTrattativa());
    }

}
