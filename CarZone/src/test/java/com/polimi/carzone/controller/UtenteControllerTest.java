package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.ModificaUtenteRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import com.polimi.carzone.persistence.service.UtenteService;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.security.TokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UtenteControllerTest {
    @Mock
    TokenUtil tokenUtil;

    @Mock
    UtenteService utenteService;

    @InjectMocks
    UtenteController utenteController;

    @Test
    void loginSuccessful() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("username");
        request.setPassword("password");
        Utente utente = new Utente();
        when(utenteService.login(request)).thenReturn(utente);
        when(tokenUtil.generaToken(utente)).thenReturn("token");
        assertAll(() -> utenteController.login(request));
    }

    @Test
    void registrazioneClienteSuccessful() {
        assertAll(() -> utenteController.registrazioneCliente(new SignupRequestDTO()));
    }

    @Test
    void registrazioneDipendenteSuccessful() {
        assertAll(() -> utenteController.registrazioneDipendente(new SignupRequestDTO()));
    }

    @Test
    void trovaUtentiManagerSuccessful() {
        assertAll(() -> utenteController.trovaUtentiManager());
    }

    @Test
    void trovaUtenteSuccessful() {
        assertAll(() -> utenteController.trovaUtente(1L));
    }

    @Test
    void modificaUtenteSuccessful() {
        assertAll(() -> utenteController.modificaUtente(1L, new ModificaUtenteRequestDTO()));
    }

    @Test
    void eliminaUtenteSuccessful() {
        assertAll(() -> utenteController.eliminaUtente(1L));
    }

    @Test
    void trovaClientiSuccessful() {
        assertAll(() -> utenteController.trovaClienti());
    }

    @Test
    void trovaDipendentiSuccessful() {
        assertAll(() -> utenteController.trovaDipendenti());
    }

}
