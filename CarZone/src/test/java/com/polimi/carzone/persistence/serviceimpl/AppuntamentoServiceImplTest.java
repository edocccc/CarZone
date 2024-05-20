package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.dto.response.DettagliVeicoloManagerResponseDTO;
import com.polimi.carzone.dto.response.RecensioneResponseDTO;
import com.polimi.carzone.exception.*;
import com.polimi.carzone.model.*;
import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.security.TokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppuntamentoServiceImplTest {

    @Mock
    AppuntamentoRepository appuntamentoRepo;
    @Mock
    UtenteRepository utenteRepo;
    @Mock
    VeicoloRepository veicoloRepo;
    @Mock
    TokenUtil tokenUtil;
    @Mock
    VeicoloService veicoloService;
    @InjectMocks
    AppuntamentoServiceImpl appuntamentoService;

    @Test
    void prenotaThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.prenota(null, null));
    }

    @Test
    void prenotaThrowsDataOraNonValida() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.prenota(new PrenotazioneRequestDTO(null, null, null), null));
    }

    @Test
    void prenotaThrowsErroriNotEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.prenota(new PrenotazioneRequestDTO(LocalDateTime.of(2025, 11, 11, 19, 30), null, null), null));
    }

    @Test
    void prenotaThrowsVeicoloNonEsiste() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.prenota(new PrenotazioneRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 3L, 3L), null));
    }

    @Test
    void prenotaThrowsVeicoloNonDisponibile() {
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili = List.of(
                new DettagliVeicoloManagerResponseDTO(1L, "targa", "marca", "modello", 123, 123, 123, Alimentazione.BENZINA, 1.0, "DISPONIBILE"));
        List<Long> veicoliDisponibiliId = List.of(2L);
        when(veicoloService.findAllDisponibili()).thenReturn(veicoliDisponibili);
        when(veicoloService.estraiIdDaFindAllDisponibili(veicoliDisponibili)).thenReturn(veicoliDisponibiliId);
        assertThrows(VeicoloNonTraDisponibiliException.class,
                () -> appuntamentoService.prenota(new PrenotazioneRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L), "tokenaaaaaaaa"));
    }

    @Test
    void prenotaThrowsDiversiId() {
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili = List.of(
                new DettagliVeicoloManagerResponseDTO(1L, "targa", "marca", "modello", 123, 123, 123, Alimentazione.BENZINA, 1.0, "DISPONIBILE"));
        List<Long> veicoliDisponibiliId = List.of(1L);
        when(veicoloService.findAllDisponibili()).thenReturn(veicoliDisponibili);
        when(veicoloService.estraiIdDaFindAllDisponibili(veicoliDisponibili)).thenReturn(veicoliDisponibiliId);
        Utente utente = new Utente();
        utente.setId(2L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.prenota(new PrenotazioneRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L), "tokenaaaaaaaa"));
    }

    @Test
    void prenotaThrowsUtenteNonTrovato() {
        when(utenteRepo.findById(any())).thenReturn(Optional.empty());
        Utente utente = new Utente();
        utente.setId(1L);
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili = List.of(
                new DettagliVeicoloManagerResponseDTO(1L, "targa", "marca", "modello", 123, 123, 123, Alimentazione.BENZINA, 1.0, "DISPONIBILE"));
        List<Long> veicoliDisponibiliId = List.of(1L);
        when(veicoloService.findAllDisponibili()).thenReturn(veicoliDisponibili);
        when(veicoloService.estraiIdDaFindAllDisponibili(veicoliDisponibili)).thenReturn(veicoliDisponibiliId);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        assertThrows(UtenteNonTrovatoException.class,
                () -> appuntamentoService.prenota(new PrenotazioneRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L), "tokenaaaaaaaa"));
    }

    @Test
    void prenotaThrowsVeicoloNonTrovato() {
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        Utente utente = new Utente();
        utente.setId(1L);
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili = List.of(
                new DettagliVeicoloManagerResponseDTO(1L, "targa", "marca", "modello", 123, 123, 123, Alimentazione.BENZINA, 1.0, "DISPONIBILE"));
        List<Long> veicoliDisponibiliId = List.of(1L);
        when(veicoloService.findAllDisponibili()).thenReturn(veicoliDisponibili);
        when(veicoloService.estraiIdDaFindAllDisponibili(veicoliDisponibili)).thenReturn(veicoliDisponibiliId);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(veicoloRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> appuntamentoService.prenota(new PrenotazioneRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L), "tokenaaaaaaaa"));
    }

    @Test
    void prenotaSuccessful() {
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili = List.of(
                new DettagliVeicoloManagerResponseDTO(1L, "targa", "marca", "modello", 123, 123, 123, Alimentazione.BENZINA, 1.0, "DISPONIBILE"));
        List<Long> veicoliDisponibiliId = List.of(1L);
        when(veicoloService.findAllDisponibili()).thenReturn(veicoliDisponibili);
        when(veicoloService.estraiIdDaFindAllDisponibili(veicoliDisponibili)).thenReturn(veicoliDisponibiliId);
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> appuntamentoService.prenota(new PrenotazioneRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L), "tokenaaaaaaaa"));
    }

    @Test
    void trovaAppuntamentiDipendenteSuccesful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        appuntamentiTrovati.get().add(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByDipendente_IdAndEsitoRegistratoIsFalse(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.trovaAppuntamentiDipendente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaAppuntamentiDipendenteThrowsIdDipendenteNonTrovato() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaAppuntamentiDipendente(null, null));
    }

    @Test
    void trovaAppuntamentiDipendenteThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.trovaAppuntamentiDipendente(1L, null));
    }

    @Test
    void trovaAppuntamentiDipendenteThrowsDiversiId() {
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.trovaAppuntamentiDipendente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaAppuntamentiClienteSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        appuntamentiTrovati.get().add(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByCliente_IdAndRecensioneVotoIsNullAndRecensioneTestoIsNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.trovaAppuntamentiCliente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaAppuntamentiClienteThrowsIdClienteNonTrovato() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaAppuntamentiCliente(null, null));
    }

    @Test
    void trovaAppuntamentiClienteThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.trovaAppuntamentiCliente(1L, null));
    }

    @Test
    void trovaAppuntamentiClienteThrowsDiversiId() {
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.trovaAppuntamentiCliente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void calcolaValutazioneMediaDipendenteSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        appuntamentiTrovati.get().add(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 5, "bravo", false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.calcolaValutazioneMediaDipendente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void calcolaValutazioneMediaSuccessfulConMinimo() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        when(appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.calcolaValutazioneMediaDipendente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void calcolaValutazioneMediaDipendenteThrowsIdDipendenteNonTrovato() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.calcolaValutazioneMediaDipendente(null, null));
    }

    @Test
    void calcolaValutazioneMediaDipendenteThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.calcolaValutazioneMediaDipendente(1L, null));
    }

    @Test
    void calcolaValutazioneMediaDipendenteThrowsDiversiId() {
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.calcolaValutazioneMediaDipendente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void calcolaValutazioneMediaDipendenteSuccessfulSenzaToken() {
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        appuntamentiTrovati.get().add(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 5, "bravo", false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.calcolaValutazioneMediaDipendente(1L));
    }

    @Test
    void calcolaValutazioneMediaSuccessfulSenzaTokenConMinimo() {
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        when(appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.calcolaValutazioneMediaDipendente(1L));
    }

    @Test
    void calcolaValutazioneMediaDipendenteSenzaTokenThrowsIdDipendenteNonTrovato() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.calcolaValutazioneMediaDipendente(null));
    }

    @Test
    void trovaAppuntamentiLiberiSuccessful() {
        Optional<List<Appuntamento>> appuntamentiLiberi = Optional.of(new ArrayList<>());
        appuntamentiLiberi.get().add(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByDipendenteIsNull()).thenReturn(appuntamentiLiberi);
        assertAll(() -> appuntamentoService.trovaAppuntamentiLiberi());
    }

    @Test
    void prendiInCaricoSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), null, new Veicolo()));
        Optional<Utente> dipendente = Optional.of(new Utente());
        when(utenteRepo.findById(any())).thenReturn(dipendente);
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        PresaInCaricoRequestDTO request = new PresaInCaricoRequestDTO();
        request.setIdDipendente(1L);
        request.setIdAppuntamento(10L);
        assertAll(() -> appuntamentoService.prendiInCarico(request, "tokenaaaaaaaa"));
    }

    @Test
    void prendiInCaricoThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.prendiInCarico(null, null));
    }

    @Test
    void prendiInCaricoThrowsErroriIsEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.prendiInCarico(new PresaInCaricoRequestDTO(), null));
    }

    @Test
    void prendiInCaricoThrowsTokenNonValido() {
        PresaInCaricoRequestDTO request = new PresaInCaricoRequestDTO();
        request.setIdDipendente(1L);
        request.setIdAppuntamento(10L);
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.prendiInCarico(request, null));
    }

    @Test
    void prendiInCaricoThrowsDiversiId() {
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        PresaInCaricoRequestDTO request = new PresaInCaricoRequestDTO();
        request.setIdDipendente(1L);
        request.setIdAppuntamento(10L);
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.prendiInCarico(request, "tokenaaaaaaaa"));
    }

    @Test
    void prendiInCaricoThrowsAppuntamentoNonTrovato() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<Utente> dipendente = Optional.of(new Utente());
        when(utenteRepo.findById(any())).thenReturn(dipendente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        PresaInCaricoRequestDTO request = new PresaInCaricoRequestDTO();
        request.setIdDipendente(1L);
        request.setIdAppuntamento(10L);
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.prendiInCarico(request, "tokenaaaaaaaa"));
    }

    @Test
    void prendiInCaricoThrowsDipendenteNonTrovato() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), null, new Veicolo()));
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        PresaInCaricoRequestDTO request = new PresaInCaricoRequestDTO();
        request.setIdDipendente(1L);
        request.setIdAppuntamento(10L);
        assertThrows(UtenteNonTrovatoException.class,
                () -> appuntamentoService.prendiInCarico(request, "tokenaaaaaaaa"));
    }

    @Test
    void prendiInCaricoThrowsAppuntamentoPresoInCarico () {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), new Utente(), new Veicolo()));
        Optional<Utente> dipendente = Optional.of(new Utente());
        when(utenteRepo.findById(any())).thenReturn(dipendente);
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        PresaInCaricoRequestDTO request = new PresaInCaricoRequestDTO();
        request.setIdDipendente(1L);
        request.setIdAppuntamento(10L);
        assertThrows(AppuntamentoPresoInCaricoException.class,
                () -> appuntamentoService.prendiInCarico(request, "tokenaaaaaaaa"));
    }

    @Test
    void prendiInCaricoThrowsAppuntamentoPassato() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2020, 11, 11, 16, 30), 4, "bravo", false, new Utente(), null, new Veicolo()));
        Optional<Utente> dipendente = Optional.of(new Utente());
        when(utenteRepo.findById(any())).thenReturn(dipendente);
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        PresaInCaricoRequestDTO request = new PresaInCaricoRequestDTO();
        request.setIdDipendente(1L);
        request.setIdAppuntamento(10L);
        assertThrows(AppuntamentoPassatoException.class,
                () -> appuntamentoService.prendiInCarico(request, "tokenaaaaaaaa"));
    }

    @Test
    void trovaIdVeicoloSuccessful() {
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), null, new Veicolo()));
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        assertAll(() -> appuntamentoService.trovaIdVeicolo(10L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaIdVeicoloThrowsIdAppuntamentoNonValido() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaIdVeicolo(null, null));
    }

    @Test
    void trovaIdVeicoloThrowsIdAppuntamentoNonTrovato() {
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.trovaIdVeicolo(1L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaIdVeicoloThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.trovaIdVeicolo(10L, null));
    }

    @Test
    void trovaIdVeicoloThrowsAppuntamentoNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.trovaIdVeicolo(10L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaIdVeicoloThrowsDiversiId() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), utente, new Veicolo()));
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.trovaIdVeicolo(10L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaIdClienteSuccessful () {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), utente, new Veicolo()));
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        assertAll(() -> appuntamentoService.trovaIdCliente(10L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaIdClienteThrowsIdAppuntamentoNonValido() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaIdCliente(null, null));
    }

    @Test
    void trovaIdClienteThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.trovaIdCliente(10L, null));
    }

    @Test
    void trovaIdClienteThrowsAppuntamentoNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.trovaIdCliente(10L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaIdClienteThrowsDiversiId() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), utente, new Veicolo()));
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.trovaIdCliente(10L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaRecensioniDipendenteSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        appuntamentiTrovati.get().add(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.trovaRecensioniDipendente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaRecensioniDipendenteThrowsIdDipendenteNonTrovato() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaRecensioniDipendente(null, null));
    }

    @Test
    void trovaRecensioniDipendenteThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.trovaRecensioniDipendente(1L, null));
    }

    @Test
    void trovaRecensioniDipendenteThrowsDiversiId() {
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.trovaRecensioniDipendente(1L, "tokenaaaaaaaa"));
    }

    @Test
    void trovaRecensioniDipendenteSenzaTokenSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        appuntamentiTrovati.get().add(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.trovaRecensioniDipendente(1L));
    }

    @Test
    void trovaRecensioniDipendenteSenzaTokenThrowsIdDipendenteNonTrovato() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaRecensioniDipendente(null));
    }

    @Test
    void trovaDipendentiConRecensioniSuccessful() {
        List<Utente> dipendenti = new ArrayList<>();
        Utente utente = new Utente();
        utente.setId(1L);
        dipendenti.add(utente);
        when(utenteRepo.findByRuolo(Ruolo.DIPENDENTE)).thenReturn(dipendenti);
        List<RecensioneResponseDTO> recensioni = new ArrayList<>();
        recensioni.add(new RecensioneResponseDTO("uno", "uno", 5, "utente"));
        assertAll(() -> appuntamentoService.trovaDipendentiConRecensioni());
    }

    @Test
    void registraVenditaSuccessful() {
        Utente utente = new Utente();
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        assertAll(() -> appuntamentoService.registraVendita(10L, true,"tokenaaaaaaaa"));
    }

    @Test
    void registraVenditaThrowsIdAppuntamentoNonValido() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.registraVendita(null, true, null));
    }

    @Test
    void registraVenditaThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.registraVendita(10L, true, null));
    }

    @Test
    void registraVenditaThrowsAppuntamentoNonTrovato() {
        Utente utente = new Utente();
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.registraVendita(10L, true, "tokenaaaaaaaa"));
    }

    @Test
    void registraVenditaThrowsDiversiId() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setRuolo(Ruolo.CLIENTE);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.registraVendita(10L, true, "tokenaaaaaaaa"));
    }

    @Test
    void registraVenditaThrowsAppuntamentoNonPassato() {
        Utente utente = new Utente();
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        assertThrows(AppuntamentoNonSvoltoException.class,
                () -> appuntamentoService.registraVendita(10L, true, "tokenaaaaaaaa"));
    }

    @Test
    void registraVenditaThrowsAppuntamentoGiaRegistrato() {
        Utente utente = new Utente();
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, true, new Utente(), new Utente(), new Veicolo())));
        assertThrows(AppuntamentoRegistratoException.class,
                () -> appuntamentoService.registraVendita(10L, true, "tokenaaaaaaaa"));
    }

    @Test
    void registraVenditaThrowsDipendenteNonAssegnato() {
        Utente utente = new Utente();
        utente.setRuolo(Ruolo.MANAGER);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), null, new Veicolo())));
        assertThrows(AppuntamentoNonAssegnatoException.class,
                () -> appuntamentoService.registraVendita(10L, false, "tokenaaaaaaaa"));
    }

    @Test
    void trovaAppuntamentiPerManagerSuccessfulConDipendente() {
        List<Appuntamento> appuntamenti = new ArrayList<>();
        appuntamenti.add(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findAll()).thenReturn(appuntamenti);
        assertAll(() -> appuntamentoService.trovaAppuntamentiPerManager());
    }

    @Test
    void trovaAppuntamentiPerManagerSuccessfulSenzaDipendente() {
        List<Appuntamento> appuntamenti = new ArrayList<>();
        appuntamenti.add(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), null, new Veicolo()));
        when(appuntamentoRepo.findAll()).thenReturn(appuntamenti);
        assertAll(() -> appuntamentoService.trovaAppuntamentiPerManager());
    }

    @Test
    void prenotaPerManagerSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> appuntamentoService.prenotaPerManager(new PrenotazioneManagerRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L, 1L)));
    }

    @Test
    void prenotaPerManagerThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.prenotaPerManager(null));
    }

    @Test
    void prenotaPerManagerThrowsErroriIsEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.prenotaPerManager(new PrenotazioneManagerRequestDTO(null, null, null, null)));
    }

    @Test
    void prenotaPerManagerThrowsVeicoloNonTrovato() {
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        when(veicoloRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> appuntamentoService.prenotaPerManager(new PrenotazioneManagerRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L, 0L)));
    }

    @Test
    void prenotaPerManagerThrowsDipendenteNonTrovato() {
        when(utenteRepo.findById(any())).thenReturn(Optional.empty());
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertThrows(UtenteNonTrovatoException.class,
                () -> appuntamentoService.prenotaPerManager(new PrenotazioneManagerRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L, 1L)));
    }

    @Test
    void prenotaPerManagerThrowsClienteNonTrovato() {
        PrenotazioneManagerRequestDTO request = new PrenotazioneManagerRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L, 0L);
        when(utenteRepo.findById(request.getIdCliente())).thenReturn(Optional.empty());
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertThrows(UtenteNonTrovatoException.class,
                () -> appuntamentoService.prenotaPerManager(request));
    }

    @Test
    void eliminaAppuntamentoSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        assertAll(() -> appuntamentoService.eliminaAppuntamento(10L));
    }

    @Test
    void eliminaAppuntamentoThrowsIdAppuntamentoNonValido() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.eliminaAppuntamento(null));
    }

    @Test
    void eliminaAppuntamentoThrowsAppuntamentoNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.eliminaAppuntamento(10L));
    }

    @Test
    void eliminaAppuntamentoThrowsAppuntamentoRegistrato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2020, 11, 11, 16, 30), null, null, true, new Utente(), new Utente(), new Veicolo())));
        assertThrows(AppuntamentoRegistratoException.class,
                () -> appuntamentoService.eliminaAppuntamento(10L));
    }

    @Test
    public void modificaAppuntamentoSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> appuntamentoService.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L,1L)));
    }

    @Test
    public void modificaAppuntamentoThrowsIdAppuntamentoNonValido() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.modificaAppuntamento(null, new ModificaAppuntamentoRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L,null)));
    }

    @Test
    public void modificaAppuntamentoThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.modificaAppuntamento(10L, null));
    }

    @Test
    public void modificaAppuntamentoThrowsErroriIsEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO(null, null, null, -1L)));
    }

    @Test
    public void modificaAppuntamentoThrowsDipendenteNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        when(utenteRepo.findById(any())).thenReturn(Optional.empty());
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertThrows(UtenteNonTrovatoException.class,
                () -> appuntamentoService.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L,1L)));
    }

    @Test
    public void modificaAppuntamentoThrowsAppuntamentoNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L,1L)));
    }

    @Test
    public void modificaAppuntamentoThrowsVeicoloNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        when(veicoloRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> appuntamentoService.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L,1L)));
    }

    @Test
    public void modificaAppuntamentoThrowsAppuntamentoRegistrato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2020, 11, 11, 16, 30), null, null, true, new Utente(), new Utente(), new Veicolo())));
        when(utenteRepo.findById(any())).thenReturn(Optional.of(new Utente()));
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertThrows(AppuntamentoRegistratoException.class,
                () -> appuntamentoService.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L,1L)));
    }

    @Test
    public void modificaAppuntamentoThrowsClienteNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, new Utente(), new Utente(), new Veicolo())));
        when(utenteRepo.findById(any())).thenReturn(Optional.empty());
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertThrows(UtenteNonTrovatoException.class,
                () -> appuntamentoService.modificaAppuntamento(10L, new ModificaAppuntamentoRequestDTO(LocalDateTime.of(2025, 11, 11, 16, 30), 1L, 1L,null)));
    }

    @Test
    public void lasciaRecensioneSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, utente, new Utente(), new Veicolo())));
        assertAll(() -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(10L, 5, "bravo"), "tokenaaaaaaaa"));
    }

    @Test
    public void lasciaRecensioneThrowsRequestNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.lasciaRecensione(null, null));
    }

    @Test
    public void lasciaRecensioneThrowsErroriIsEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(null, null, null), null));
    }

    @Test
    public void lasciaRecensioneThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(10L, 5, "bravo"), null));
    }

    @Test
    public void lasciaRecensioneThrowsAppuntamentoNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(10L, 5, "bravo"), "tokenaaaaaaaa"));
    }

    @Test
    public void lasciaRecensioneThrowsAppuntamentoNonSvolto() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), null, null, false, utente, new Utente(), new Veicolo())));
        assertThrows(AppuntamentoNonSvoltoException.class,
                () -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(10L, 5, "bravo"), "tokenaaaaaaaa"));
    }

    @Test
    public void lasciaRecensioneThrowsAppuntamentoNonTuo() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Utente utente2 = new Utente();
        utente2.setId(2L);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), null, null, false, utente2, new Utente(), new Veicolo())));
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(10L, 5, "bravo"), "tokenaaaaaaaa"));
    }

    @Test
    public void lasciaRecensioneThrowsRecensioneGiaLasciata() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), 5, "bravo", false, utente, new Utente(), new Veicolo())));
        assertThrows(RecensioneGiaLasciataException.class,
                () -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(10L, 5, "bravo"), "tokenaaaaaaaa"));
    }

    @Test
    public void lasciaRecensioneThrowsDipendenteNonAssegnato() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(new Appuntamento(10L, LocalDateTime.of(2023, 11, 11, 16, 30), null, null, false, utente, null, new Veicolo())));
        assertThrows(DipendenteNonAssegnatoException.class,
                () -> appuntamentoService.lasciaRecensione(new LasciaRecensioneRequestDTO(10L, 5, "bravo"), "tokenaaaaaaaa"));
    }

    @Test
    public void trovaRecensioniClienteSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(utente);
        Optional<List<Appuntamento>> appuntamentiTrovati = Optional.of(new ArrayList<>());
        appuntamentiTrovati.get().add(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), 5, "bravo", false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findByCliente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(1L)).thenReturn(appuntamentiTrovati);
        assertAll(() -> appuntamentoService.trovaRecensioniCliente(1L, "tokenaaaaaaaa"));
    }

    @Test
    public void trovaRecensioniClienteThrowsIdClienteNonTrovato() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaRecensioniCliente(null, null));
    }

    @Test
    public void trovaRecensioniClienteThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.trovaRecensioniCliente(1L, null));
    }

    @Test
    public void trovaRecensioniClienteThrowsDiversiId() {
        when(tokenUtil.getUtenteFromToken(any())).thenReturn(new Utente());
        assertThrows(DiversiIdException.class,
                () -> appuntamentoService.trovaRecensioniCliente(1L, "tokenaaaaaaaa"));
    }

    @Test
    public void trovaPerModificaSuccessful() {
        Utente utente = new Utente();
        utente.setId(1L);
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", false, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        assertAll(() -> appuntamentoService.trovaPerModifica(10L, "tokenaaaaaaaa"));
    }

    @Test
    public void trovaPerModificaThrowsIdAppuntamentoNonValido() {
        assertThrows(CredenzialiNonValideException.class,
                () -> appuntamentoService.trovaPerModifica(null, null));
    }

    @Test
    public void trovaPerModificaThrowsTokenNonValido() {
        assertThrows(TokenNonValidoException.class,
                () -> appuntamentoService.trovaPerModifica(10L, null));
    }

    @Test
    public void trovaPerModificaThrowsAppuntamentoNonTrovato() {
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> appuntamentoService.trovaPerModifica(10L, "tokenaaaaaaaa"));
    }

    @Test
    public void trovaPerModificaThrowsEsitoGiaRegistrato() {
        Optional<Appuntamento> appuntamento = Optional.of(new Appuntamento(10L, LocalDateTime.of(2025, 11, 11, 16, 30), 4, "bravo", true, new Utente(), new Utente(), new Veicolo()));
        when(appuntamentoRepo.findById(any())).thenReturn(appuntamento);
        assertThrows(AppuntamentoRegistratoException.class,
                () -> appuntamentoService.trovaPerModifica(10L, "tokenaaaaaaaa"));
    }


}
