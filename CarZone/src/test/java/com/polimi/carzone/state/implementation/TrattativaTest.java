package com.polimi.carzone.state.implementation;

import com.polimi.carzone.model.Veicolo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class TrattativaTest {
    @InjectMocks
    private Trattativa trattativa;

    @Test
    void onDisponibileSuccessful() {
        Veicolo veicolo = new Veicolo();
        trattativa = new Trattativa(veicolo);
        assertAll(() -> trattativa.onDisponibile());
    }

    @Test
    void onVendutoSuccessful() {
        Veicolo veicolo = new Veicolo();
        trattativa = new Trattativa(veicolo);
        assertAll(() -> trattativa.onVenduto());
    }

    @Test
    void onTrattativaSuccessful() {
        Veicolo veicolo = new Veicolo();
        trattativa = new Trattativa(veicolo);
        assertAll(() -> trattativa.onTrattativa());
    }
}
