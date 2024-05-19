package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;
import com.polimi.carzone.dto.request.PresaInCaricoRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloManagerResponseDTO;
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

}
