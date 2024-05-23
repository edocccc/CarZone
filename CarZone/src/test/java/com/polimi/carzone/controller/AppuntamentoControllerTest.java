package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class AppuntamentoControllerTest {
    @Mock
    AppuntamentoService appuntamentoService;

    @InjectMocks
    AppuntamentoController appuntamentoController;

    @Test
    void prenotaSuccessful(){
        assertAll(() -> appuntamentoController.prenota(new PrenotazioneRequestDTO(), "tokenaaaaaa"));
    }

    @Test
    void trovaAppuntamentiDipendenteSuccessful() {
        assertAll(() -> appuntamentoController.trovaAppuntamentiDipendente(1L, "tokenaaaaaa"));
    }

    @Test
    void calcolaValutazioneMediaDipendenteSuccessful() {
        assertAll(() -> appuntamentoController.calcolaValutazioneMediaDipendente(1L, "tokenaaaaaa"));
    }

    @Test
    void trovaAppuntamentiLiberiSuccessful() {
        assertAll(() -> appuntamentoController.trovaAppuntamentiLiberi());
    }

    @Test
    void prendiInCaricoSuccessful() {
        assertAll(() -> appuntamentoController.prendiInCarico(new PresaInCaricoRequestDTO(), "tokenaaaaaa"));
    }

    @Test
    void trovaRecensioniDipendenteSuccessful() {
        assertAll(() -> appuntamentoController.trovaRecensioniDipendente(1L, "tokenaaaaaa"));
    }

    @Test
    void trovaDipendentiConRecensioniSuccessful() {
        assertAll(() -> appuntamentoController.trovaDipendentiConRecensioni());
    }

    @Test
    void trovaAppuntamentiPerManagerSuccessful() {
        assertAll(() -> appuntamentoController.trovaAppuntamentiPerManager());
    }

    @Test
    void prenotaPerManagerSuccessful() {
        assertAll(() -> appuntamentoController.prenotaPerManager(new PrenotazioneManagerRequestDTO()));
    }

    @Test
    void eliminaUtenteSuccessful() {
        assertAll(() -> appuntamentoController.eliminaUtente(10L));
    }

    @Test
    void modificaAppuntamentoSuccessful() {
        assertAll(() -> appuntamentoController.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO()));
    }

    @Test
    void trovaAppuntamentiClienteSuccessful() {
        assertAll(() -> appuntamentoController.trovaAppuntamentiCliente(1L, "tokenaaaaaa"));
    }

    @Test
    void lasciaRecensioneSuccessful() {
        assertAll(() -> appuntamentoController.lasciaRecensione(new LasciaRecensioneRequestDTO(), "tokenaaaaaa"));
    }

    @Test
    void trovaRecensioniClienteSuccessful() {
        assertAll(() -> appuntamentoController.trovaRecensioniCliente(1L, "tokenaaaaaa"));
    }

    @Test
    void trovaPerModificaSuccessful() {
        assertAll(() -> appuntamentoController.trovaPerModifica(10L, "tokenaaaaaa"));
    }

}
