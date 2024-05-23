package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.ModificaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.RegistrazioneVenditaRequestDTO;
import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import com.polimi.carzone.persistence.service.UtenteService;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.context.RicercaManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class VeicoloControllerTest {
    @Mock
    VeicoloService veicoloService;

    @Mock
    AppuntamentoService appuntamentoService;

    @Mock
    UtenteService utenteService;

    @Mock
    RicercaManager ricercaManager;

    @InjectMocks
    VeicoloController veicoloController;

    @Test
    void aggiungiVeicoloSuccessful() {
        assertAll(() -> veicoloController.aggiungiVeicolo(new AggiuntaVeicoloRequestDTO()));
    }

    @Test
    void stampaVeicoliSuccessful() {
        assertAll(() -> veicoloController.stampaVeicoli());
    }

    @Test
    void mostraDettagliSuccessful() {
        assertAll(() -> veicoloController.mostraDettagli(1L));
    }

    @Test
    void cercaVeicoliSuccessful() {
        assertAll(() -> veicoloController.cercaVeicoli(new RicercaRequestDTO()));
    }

    @Test
    void registraVenditaTrueSuccessful() {
        RegistrazioneVenditaRequestDTO request = new RegistrazioneVenditaRequestDTO();
        request.setIdAppuntamento(10L);
        request.setVenditaConclusa(true);
        assertAll(() -> veicoloController.registraVendita(request, "tokenaaaaaa"));
    }

    @Test
    void registraVenditaFalseSuccessful() {
        RegistrazioneVenditaRequestDTO request = new RegistrazioneVenditaRequestDTO();
        request.setIdAppuntamento(10L);
        request.setVenditaConclusa(false);
        assertAll(() -> veicoloController.registraVendita(request, "tokenaaaaaa"));
    }

    @Test
    void stampaVeicoliConDettagliSuccessful() {
        assertAll(() -> veicoloController.stampaVeicoliConDettagli());
    }

    @Test
    void eliminaVeicoloSuccessful() {
        assertAll(() -> veicoloController.eliminaVeicolo(1L));
    }

    @Test
    void modificaVeicoloSuccessful() {
        assertAll(() -> veicoloController.modificaVeicolo(1L,new ModificaVeicoloRequestDTO()));
    }

    @Test
    void stampaVeicoliDisponibiliPerManagerSuccessful() {
        assertAll(() -> veicoloController.stampaVeicoliDiponibiliPerManager());
    }

    @Test
    void stampaVeicoliDisponibiliESelezionatoPerManagerSuccessful() {
        assertAll(() -> veicoloController.stampaVeicoliDiponibiliESelezionatoPerManager(1L));
    }

}
