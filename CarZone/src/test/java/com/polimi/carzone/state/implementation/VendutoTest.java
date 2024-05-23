package com.polimi.carzone.state.implementation;

import com.polimi.carzone.model.Veicolo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class VendutoTest {
    @InjectMocks
    private Venduto venduto;

    @Test
    void onDisponibileSuccessful() {
        Veicolo veicolo = new Veicolo();
        venduto = new Venduto(veicolo);
        assertAll(() -> venduto.onDisponibile());
    }

    @Test
    void onVendutoSuccessful() {
        Veicolo veicolo = new Veicolo();
        venduto = new Venduto(veicolo);
        assertAll(() -> venduto.onVenduto());
    }

    @Test
    void onTrattativaSuccessful() {
        Veicolo veicolo = new Veicolo();
        venduto = new Venduto(veicolo);
        assertAll(() -> venduto.onTrattativa());
    }
}
