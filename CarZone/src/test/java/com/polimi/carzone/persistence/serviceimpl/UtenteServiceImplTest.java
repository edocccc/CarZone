package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.ModificaUtenteRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.exception.CredenzialiNonValideException;
import com.polimi.carzone.exception.RuoloNonValidoException;
import com.polimi.carzone.exception.UtenteNonTrovatoException;
import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UtenteServiceImplTest {

    @Mock
    UtenteRepository utenteRepo;

    @InjectMocks
    UtenteServiceImpl utenteService;

    @Test
    void findByUsernameSuccessful() {
        when(utenteRepo.findByUsername("username")).thenReturn(Optional.of(new Utente()));
        assertAll(() -> utenteService.findByUsername("username"));
    }

    @Test
    void findByUsernameThrowsUtenteNonTrovato() {
        when(utenteRepo.findByUsername("username")).thenReturn(Optional.empty());
        assertThrows(UtenteNonTrovatoException.class,
                () -> utenteService.findByUsername("username"));
    }

    @Test
    void loginSuccessful() {
        when(utenteRepo.findByUsername(any())).thenReturn(Optional.of(new Utente()));
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("username");
        request.setPassword("password");
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        ReflectionTestUtils.setField(utenteService, "passwordEncoder", passwordEncoderMock);
        when(passwordEncoderMock.matches(any(),any())).thenReturn(true);
        assertAll(() -> utenteService.login(request));
    }

    @Test
    void loginThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.login(null));
    }

    @Test
    void loginThrowsUtenteNonTrovato() {
        when(utenteRepo.findByUsername(any())).thenReturn(Optional.empty());
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("username");
        request.setPassword("password");
        assertThrows(UtenteNonTrovatoException.class,
                () -> utenteService.login(request));
    }

    @Test
    void loginThrowsUtenteNonTrovatoDaPassword() {
        Utente utente = new Utente();
        utente.setPassword("password");
        when(utenteRepo.findByUsername(any())).thenReturn(Optional.of(utente));
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("username");
        request.setPassword("ciao");
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        ReflectionTestUtils.setField(utenteService, "passwordEncoder", passwordEncoderMock);
        when(passwordEncoderMock.matches(any(),any())).thenReturn(false);
        assertThrows(UtenteNonTrovatoException.class,
                () -> utenteService.login(request));
    }

    @Test
    void loginThrowsErroriIsEmpty() {
        LoginRequestDTO request = new LoginRequestDTO();
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.login(request));
    }

    @Test
    void registrazioneClienteSuccessful() {
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        when(passwordEncoderMock.encode(any())).thenReturn("passwordEncoded");
        when(utenteRepo.save(any())).thenReturn(new Utente());
        ReflectionTestUtils.setField(utenteService, "passwordEncoder", passwordEncoderMock);
        SignupRequestDTO request = new SignupRequestDTO();
        request.setUsername("username");
        request.setPassword("password");
        request.setPasswordRipetuta("password");
        request.setDataNascita(LocalDate.of(2002, 6, 3));
        request.setNome("nome");
        request.setCognome("cognome");
        request.setEmail("email@gmail.com");
        assertAll(() -> utenteService.registrazioneCliente(request));
    }

    @Test
    void registrazioneClienteThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.registrazioneCliente(null));
    }

    @Test
    void registrazioneClienteThrowsUsernameInUso() {
        SignupRequestDTO request = new SignupRequestDTO();
        request.setUsername("username");
        request.setPassword("password");
        request.setPasswordRipetuta("password");
        request.setDataNascita(LocalDate.of(2002, 6, 3));
        request.setNome("nome");
        request.setCognome("cognome");
        request.setEmail("email@gmail.com");
        when(utenteRepo.findByUsername(any())).thenReturn(Optional.of(new Utente()));
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.registrazioneCliente(request));
    }

    @Test
    void registrazioneClienteThrowsErroriIsEmpty() {
        SignupRequestDTO request = new SignupRequestDTO();
        when(utenteRepo.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.registrazioneCliente(request));
    }

    @Test
    void registrazioneClienteThrowsPasswordNonCorrispondenti() {
        SignupRequestDTO request = new SignupRequestDTO();
        request.setUsername("username");
        request.setPassword("password");
        request.setPasswordRipetuta("");
        request.setDataNascita(LocalDate.of(2002, 6, 3));
        request.setNome("nome");
        request.setCognome("cognome");
        request.setEmail("");
        when(utenteRepo.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.registrazioneCliente(request));
    }

    @Test
    void registrazioneDipendenteSuccessful() {
        when(utenteRepo.save(any())).thenReturn(new Utente());
        SignupRequestDTO request = new SignupRequestDTO();
        request.setUsername("username");
        request.setPassword("password");
        request.setPasswordRipetuta("password");
        request.setDataNascita(LocalDate.of(2002, 6, 3));
        request.setNome("nome");
        request.setCognome("cognome");
        request.setEmail("email@gmail.com");
        assertAll(() -> utenteService.registrazioneDipendente(request));
    }

    @Test
    void findByIdSuccessful() {
        when(utenteRepo.findById(1L)).thenReturn(Optional.of(new Utente()));
        assertAll(() -> utenteService.findById(1L));
    }

    @Test
    void findByIdThrowsIdNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.findById(0L));
    }

    @Test
    void findByIdThrowsUtenteNonTrovato() {
        when(utenteRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UtenteNonTrovatoException.class,
                () -> utenteService.findById(1L));
    }

    @Test
    void trovaUtentiManagerSuccessful() {
        List<Utente> utenti = new ArrayList<>();
        utenti.add(new Utente());
        when(utenteRepo.findAll()).thenReturn(utenti);
        assertAll(() -> utenteService.trovaUtentiManager());
    }

    @Test
    void trovaUtenteManagerSuccessful() {
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        assertAll(() -> utenteService.trovaUtenteManager(1L));
    }

    @Test
    void trovaUtenteManagerThrowsIdNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.trovaUtenteManager(null));
    }

    @Test
    void trovaUtenteManagerThrowsUtenteNonTrovato() {
        when(utenteRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(UtenteNonTrovatoException.class,
                () -> utenteService.trovaUtenteManager(1L));
    }

    @Test
    void modificaUtenteDipendenteSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.DIPENDENTE);
        when(utenteRepo.findById(any())).thenReturn(Optional.of(utente));
        when(utenteRepo.save(any())).thenReturn(new Utente());
        ModificaUtenteRequestDTO request = new ModificaUtenteRequestDTO( "email@gmail.com", "nome", "cognome", "username", LocalDate.of(2002, 6, 3), "DIPENDENTE");
        assertAll(() -> utenteService.modificaUtente(1L, request));
    }

    @Test
    void modificaUtenteClienteSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.CLIENTE);
        when(utenteRepo.findById(any())).thenReturn(Optional.of(utente));
        when(utenteRepo.save(any())).thenReturn(new Utente());
        ModificaUtenteRequestDTO request = new ModificaUtenteRequestDTO( "email@gmail.com", "nome", "cognome", "username", LocalDate.of(2002, 6, 3), "CLIENTE");
        assertAll(() -> utenteService.modificaUtente(1L, request));
    }

    @Test
    void modificaUtenteThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.modificaUtente(null, null));
    }

    @Test
    void modificaUtenteThrowsErroriIsEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.modificaUtente(null, new ModificaUtenteRequestDTO(null, null, null, null, null, null)));
    }

    @Test
    void modificaUtenteThrowsUtenteNonTrovato() {
        when(utenteRepo.findById(any())).thenReturn(Optional.empty());
        ModificaUtenteRequestDTO request = new ModificaUtenteRequestDTO("e@g.com", "nome", "cognome", "usr", LocalDate.of(2002, 6, 3), "MANAGER");
        assertThrows(UtenteNonTrovatoException.class,
                () -> utenteService.modificaUtente(1L, request));
    }

    @Test
    void modificaUtenteThrowsRuoloInputNonValido() {
        ModificaUtenteRequestDTO request = new ModificaUtenteRequestDTO("e@g.com", "nome", "cognome", "usr", LocalDate.of(2002, 6, 3), "CIAO");
        assertThrows(RuoloNonValidoException.class,
                () -> utenteService.modificaUtente(1L, request));
    }

    @Test
    void modificaUtenteThrowsRuoloUtenteNonValido() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.MANAGER);
        when(utenteRepo.findById(any())).thenReturn(Optional.of(utente));
        ModificaUtenteRequestDTO request = new ModificaUtenteRequestDTO("e@g.com", "nome", "cognome", "usr", LocalDate.of(2002, 6, 3), "MANAGER");
        assertThrows(RuoloNonValidoException.class,
                () -> utenteService.modificaUtente(1L, request));
    }

    @Test
    void eliminaUtenteSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.CLIENTE);
        when(utenteRepo.findById(any())).thenReturn(Optional.of(utente));
        assertAll(() -> utenteService.eliminaUtente(1L));
    }

    @Test
    void eliminaUtenteThrowsIdNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> utenteService.eliminaUtente(null));
    }

    @Test
    void eliminaUtenteThrowsUtenteNonTrovato() {
        when(utenteRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(UtenteNonTrovatoException.class,
                () -> utenteService.eliminaUtente(1L));
    }

    @Test
    void eliminaUtenteThrowsRuoloUtenteNonValido() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.MANAGER);
        when(utenteRepo.findById(any())).thenReturn(Optional.of(utente));
        assertThrows(RuoloNonValidoException.class,
                () -> utenteService.eliminaUtente(1L));
    }

    @Test
    void trovaClientiSuccessful() {
        List<Utente> utenti = new ArrayList<>();
        Utente utente = new Utente();
        utente.setRuolo(Ruolo.CLIENTE);
        utenti.add(utente);
        when(utenteRepo.findAll()).thenReturn(utenti);
        assertAll(() -> utenteService.trovaClienti());
    }

    @Test
    void trovaDipendenti() {
        List<Utente> utenti = new ArrayList<>();
        Utente utente = new Utente();
        utente.setRuolo(Ruolo.DIPENDENTE);
        utenti.add(utente);
        when(utenteRepo.findAll()).thenReturn(utenti);
        assertAll(() -> utenteService.trovaDipendenti());
    }

}
