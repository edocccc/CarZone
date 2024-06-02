package com.polimi.carzone.dto;

import com.polimi.carzone.dto.response.*;
import com.polimi.carzone.model.*;
import com.polimi.carzone.state.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ResponseTest {
    @Test
    void aggiuntaVeicoloTest() {
        AggiuntaVeicoloResponseDTO response = new AggiuntaVeicoloResponseDTO("Veicolo aggiunto con successo");
        String messaggio = response.getMessaggio();

        AppuntamentoConRecensioneResponseDTO appuntamentoConRecensioneResponseDTO = new AppuntamentoConRecensioneResponseDTO(1L, LocalDateTime.MAX, "nomeCliente", "cognomeCliente", "nomeDipendente", "cognomeDipendente", "targaVeicolo", "marcaVeicolo", "modelloVeicolo", 5, "recensioneTesto", true, true);
        long id = appuntamentoConRecensioneResponseDTO.getId();
        LocalDateTime dataOra = appuntamentoConRecensioneResponseDTO.getDataOra();
        String nomeCliente = appuntamentoConRecensioneResponseDTO.getNomeCliente();
        String cognomeCliente = appuntamentoConRecensioneResponseDTO.getCognomeCliente();
        String nomeDipendente = appuntamentoConRecensioneResponseDTO.getNomeDipendente();
        String cognomeDipendente = appuntamentoConRecensioneResponseDTO.getCognomeDipendente();
        String targaVeicolo = appuntamentoConRecensioneResponseDTO.getTargaVeicolo();
        String marcaVeicolo = appuntamentoConRecensioneResponseDTO.getMarcaVeicolo();
        String modelloVeicolo = appuntamentoConRecensioneResponseDTO.getModelloVeicolo();
        int recensioneVoto = appuntamentoConRecensioneResponseDTO.getRecensioneVoto();
        String recensioneTesto = appuntamentoConRecensioneResponseDTO.getRecensioneTesto();
        boolean esitoRegistrato = appuntamentoConRecensioneResponseDTO.isEsitoRegistrato();
        boolean dataPassata = appuntamentoConRecensioneResponseDTO.isDataPassata();
    }

    @Test
    void appuntamentoManagerResponseDTOTest() {

        AppuntamentoManagerResponseDTO appuntmentoNoArgs = new AppuntamentoManagerResponseDTO();
        AppuntamentoManagerResponseDTO appuntamentoManagerResponseDTO = new AppuntamentoManagerResponseDTO(1L, LocalDateTime.MAX, "nomeCliente", "cognomeCliente", "nomeDipendente", "cognomeDipendente", "targaVeicolo", "marcaVeicolo", "modelloVeicolo", true, true);
        long id = appuntamentoManagerResponseDTO.getId();
        LocalDateTime dataOra = appuntamentoManagerResponseDTO.getDataOra();
        String nomeCliente = appuntamentoManagerResponseDTO.getNomeCliente();
        String cognomeCliente = appuntamentoManagerResponseDTO.getCognomeCliente();
        String nomeDipendente = appuntamentoManagerResponseDTO.getNomeDipendente();
        String cognomeDipendente = appuntamentoManagerResponseDTO.getCognomeDipendente();
        String targaVeicolo = appuntamentoManagerResponseDTO.getTargaVeicolo();
        String marcaVeicolo = appuntamentoManagerResponseDTO.getMarcaVeicolo();
        String modelloVeicolo = appuntamentoManagerResponseDTO.getModelloVeicolo();
        boolean esitoRegistrato = appuntamentoManagerResponseDTO.isEsitoRegistrato();
        boolean dataPassata = appuntamentoManagerResponseDTO.isDataPassata();

    }

    @Test
    void appuntamentoModificaTest() {
        AppuntamentoModificaResponseDTO appuntamentoModificaResponseDTO = new AppuntamentoModificaResponseDTO(LocalDateTime.MAX, 1L, "nomeCliente", "cognomeCliente", 1L, "targaVeicolo", "marcaVeicolo", "modelloVeicolo", 1L, "nomeDipendente", "cognomeDipendente");
        LocalDateTime dataOra = appuntamentoModificaResponseDTO.getDataOra();
        Long idCliente = appuntamentoModificaResponseDTO.getIdCliente();
        String nomeCliente = appuntamentoModificaResponseDTO.getNomeCliente();
        String cognomeCliente = appuntamentoModificaResponseDTO.getCognomeCliente();
        Long idVeicolo = appuntamentoModificaResponseDTO.getIdVeicolo();
        String targaVeicolo = appuntamentoModificaResponseDTO.getTargaVeicolo();
        String marcaVeicolo = appuntamentoModificaResponseDTO.getMarcaVeicolo();
        String modelloVeicolo = appuntamentoModificaResponseDTO.getModelloVeicolo();
        Long idDipendente = appuntamentoModificaResponseDTO.getIdDipendente();
        String nomeDipendente = appuntamentoModificaResponseDTO.getNomeDipendente();
        String cognomeDipendente = appuntamentoModificaResponseDTO.getCognomeDipendente();

    }

    @Test
    void appuntamentoResponseTest() {
        AppuntamentoResponseDTO appuntamentoResponseDTO = new AppuntamentoResponseDTO(1L, LocalDateTime.MAX, "nomeCliente", "cognomeCliente", "targaVeicolo", "marcaVeicolo", "modelloVeicolo", true);
        long id = appuntamentoResponseDTO.getId();
        LocalDateTime dataOra = appuntamentoResponseDTO.getDataOra();
        String nomeCliente = appuntamentoResponseDTO.getNomeCliente();
        String cognomeCliente = appuntamentoResponseDTO.getCognomeCliente();
        String targaVeicolo = appuntamentoResponseDTO.getTargaVeicolo();
        String marcaVeicolo = appuntamentoResponseDTO.getMarcaVeicolo();
        String modelloVeicolo = appuntamentoResponseDTO.getModelloVeicolo();
        boolean dataPassata = appuntamentoResponseDTO.isDataPassata();

    }

    @Test
    void dettagliVeicoloManagertest() {
        DettagliVeicoloManagerResponseDTO dettagliVeicoloManagerResponseDTO = new DettagliVeicoloManagerResponseDTO(1L, "targa", "marca", "modello", 1, 1, 1, Alimentazione.BENZINA, 1.0, "stato", new byte[1]);
        long id = dettagliVeicoloManagerResponseDTO.getId();
        String targa = dettagliVeicoloManagerResponseDTO.getTarga();
        String marca = dettagliVeicoloManagerResponseDTO.getMarca();
        String modello = dettagliVeicoloManagerResponseDTO.getModello();
        int chilometraggio = dettagliVeicoloManagerResponseDTO.getChilometraggio();
        int annoProduzione = dettagliVeicoloManagerResponseDTO.getAnnoProduzione();
        int potenzaCv = dettagliVeicoloManagerResponseDTO.getPotenzaCv();
        Alimentazione alimentazione = dettagliVeicoloManagerResponseDTO.getAlimentazione();
        double prezzo = dettagliVeicoloManagerResponseDTO.getPrezzo();
        String stato = dettagliVeicoloManagerResponseDTO.getStato();
        byte[] immagine = dettagliVeicoloManagerResponseDTO.getImmagine();
    }

    @Test
    void dettagliVeicoloTest() {
        DettagliVeicoloResponseDTO dettagliNoArgs = new DettagliVeicoloResponseDTO();
        DettagliVeicoloResponseDTO dettagli = new DettagliVeicoloResponseDTO(1L, "targa", "marca", "modello", 1, 1, 1, Alimentazione.BENZINA, 1.0);
        long id = dettagli.getId();
        String targa = dettagli.getTarga();
        String marca = dettagli.getMarca();
        String modello = dettagli.getModello();
        int chilometraggio = dettagli.getChilometraggio();
        int annoProduzione = dettagli.getAnnoProduzione();
        int potenzaCv = dettagli.getPotenzaCv();
        Alimentazione alimentazione = dettagli.getAlimentazione();
        double prezzo = dettagli.getPrezzo();

    }

    @Test
    void valutazioneMediaTest() {
        ValutazioneMediaResponseDTO valutazioneMediaResponseDTO = new ValutazioneMediaResponseDTO();
        double valutazioneMedia = valutazioneMediaResponseDTO.getValutazioneMedia();
    }

    @Test
    void UtenteManagerTest() {
        UtenteManagerResponseDTO utenteManagerResponseDTO = new UtenteManagerResponseDTO(1L, "email", "nome", "cognome", "username", LocalDate.MAX, null);
        long id = utenteManagerResponseDTO.getId();
        String email = utenteManagerResponseDTO.getEmail();
        String nome = utenteManagerResponseDTO.getNome();
        String cognome = utenteManagerResponseDTO.getCognome();
        String username = utenteManagerResponseDTO.getUsername();
        LocalDate dataNascita = utenteManagerResponseDTO.getDataNascita();
        Ruolo ruolo = utenteManagerResponseDTO.getRuolo();
    }

    @Test
    void registrazioneVenditaTest() {
        RegistrazioneVenditaResponseDTO registrazioneVenditaResponseDTO = new RegistrazioneVenditaResponseDTO();
        String messaggio = registrazioneVenditaResponseDTO.getMessaggio();
    }

    @Test
    void registrazioneTest() {
        RegistrazioneResponseDTO registrazioneResponseDTO = new RegistrazioneResponseDTO("Registrazione avvenuta con successo");
        String messaggio = registrazioneResponseDTO.getMessaggio();
    }

    @Test
    void recensioneTest() {
        RecensioneResponseDTO recensioneResponseDTO = new RecensioneResponseDTO("nomeCliente", "cognomeCliente", 5, "testo");
        String nomeCliente = recensioneResponseDTO.getNomeCliente();
        String cognomeCliente = recensioneResponseDTO.getCognomeCliente();
        Integer voto = recensioneResponseDTO.getVoto();
        String testo = recensioneResponseDTO.getTesto();
    }

    @Test
    void recensioneClienteTest() {
        RecensioneClienteResponseDTO recensioneClienteNoArgs = new RecensioneClienteResponseDTO();
        RecensioneClienteResponseDTO recensioneClienteResponseDTO = new RecensioneClienteResponseDTO("nomeDipendente", "cognomeDipendente", 5, "testo");
        String nomeDipendente = recensioneClienteResponseDTO.getNomeDipendente();
        String cognomeDipendente = recensioneClienteResponseDTO.getCognomeDipendente();
        int recensioneVoto = recensioneClienteResponseDTO.getRecensioneVoto();
        String recensioneTesto = recensioneClienteResponseDTO.getRecensioneTesto();
    }

    @Test
    void presaInCaricoTest() {
        PresaInCaricoResponseDTO presaInCaricoResponseDTO = new PresaInCaricoResponseDTO();
        String message = presaInCaricoResponseDTO.getMessage();
    }

    @Test
    void prenotazioneTest() {
        PrenotazioneResponseDTO prenotazioneResponseDTO = new PrenotazioneResponseDTO("Prenotazione avvenuta con successo");
        String messaggio = prenotazioneResponseDTO.getMessaggio();
    }

    @Test
    void veicoloTest() {
        Veicolo veicolo = new Veicolo();
        String nomeImmagine = veicolo.getNomeImmagine();
        String tipoImmagine = veicolo.getTipoImmagine();
        List<Appuntamento> appuntamentiVeicolo = veicolo.getAppuntamentiVeicolo();
        State statoVeicolo = veicolo.getStato();
        VeicoloResponseDTO veicoloResponseDTO = new VeicoloResponseDTO(1L, "marca", "modello", 1.0, "stato", new byte[1]);
        long id = veicoloResponseDTO.getId();
        String marca = veicoloResponseDTO.getMarca();
        String modello = veicoloResponseDTO.getModello();
        double prezzo = veicoloResponseDTO.getPrezzo();
        String stato = veicoloResponseDTO.getStato();
        byte[] immagine = veicoloResponseDTO.getImmagine();
    }

    @Test
    void dipendenteConRecensioneTest() {
        DipendenteConRecensioneDTO dipendenteConRecensioneDTO = new DipendenteConRecensioneDTO("nomeDipendente", "cognomeDipendente", new ValutazioneMediaResponseDTO(), null);
        String nomeDipendente = dipendenteConRecensioneDTO.getNomeDipendente();
        String cognomeDipendente = dipendenteConRecensioneDTO.getCognomeDipendente();
        ValutazioneMediaResponseDTO valutazioneMedia = dipendenteConRecensioneDTO.getValutazioneMedia();
        List<RecensioneResponseDTO> recensioni = dipendenteConRecensioneDTO.getRecensioni();
    }

    @Test
    void eliminaUtenteTest() {
        EliminaUtenteResponseDTO eliminaUtenteResponseDTO = new EliminaUtenteResponseDTO("Utente eliminato con successo");
        String messaggio = eliminaUtenteResponseDTO.getMessaggio();
    }

    @Test
    void eliminaVeicoloTest() {
        EliminaVeicoloResponseDTO eliminaVeicoloResponseDTO = new EliminaVeicoloResponseDTO("Veicolo eliminato con successo");
        String messaggio = eliminaVeicoloResponseDTO.getMessaggio();
    }

    @Test
    void exceptionResponse() {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        LocalDateTime timestamp = exceptionResponseDTO.getTimestamp();
        Map<String,String> errori = exceptionResponseDTO.getErrori();
    }

    @Test
    void lasciaRecensioneTest() {
        LasciaRecensioneResponseDTO lasciaRecensioneNoArgs = new LasciaRecensioneResponseDTO();
        LasciaRecensioneResponseDTO lasciaRecensioneResponseDTO = new LasciaRecensioneResponseDTO("Recensione lasciata con successo");
        String messaggio = lasciaRecensioneNoArgs.getMessaggio();
    }

    @Test
    void loginTest() {
        Utente utente = new Utente( 1L, "email", "nome", "cognome", "username", "password", LocalDate.MAX, Ruolo.CLIENTE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(1L, "email", "nome", "cognome", "username", LocalDate.MAX, Ruolo.CLIENTE, "token");
        long id = loginResponseDTO.getId();
        String email = loginResponseDTO.getEmail();
        String nome = loginResponseDTO.getNome();
        String cognome = loginResponseDTO.getCognome();
        String username = loginResponseDTO.getUsername();
        LocalDate dataNascita = loginResponseDTO.getDataNascita();
        Ruolo ruolo = loginResponseDTO.getRuolo();
        String token = loginResponseDTO.getToken();
    }

    @Test
    void modificaAppuntamentoTest() {
        ModificaAppuntamentoResponseDTO modificaAppuntamentoResponseDTO = new ModificaAppuntamentoResponseDTO("Appuntamento modificato con successo");
        String messaggio = modificaAppuntamentoResponseDTO.getMessage();
    }

    @Test
    void modificaUtenteTest() {
        ModificaUtenteResponseDTO modificaUtenteResponseDTO = new ModificaUtenteResponseDTO("Utente modificato con successo");
        String messaggio = modificaUtenteResponseDTO.getMessage();

        Utente utente = new Utente();
        List<Veicolo> veicoli = utente.getVeicoliAcquistati();
        List<Appuntamento> appuntamentiCliente = utente.getAppuntamentiCliente();
        List<Appuntamento> appuntamentiDipendente = utente.getAppuntamentiDipendente();
        Collection<? extends GrantedAuthority> authorities = utente.getAuthorities();
        boolean accountNonExpired = utente.isAccountNonExpired();
        boolean accountNonLocked = utente.isAccountNonLocked();
        boolean credentialsNonExpired = utente.isCredentialsNonExpired();
        boolean enabled = utente.isEnabled();

    }

    @Test
    void modificaVeicoloTest() {
        ModificaVeicoloResponseDTO modificaVeicoloResponseDTO = new ModificaVeicoloResponseDTO("Veicolo modificato con successo");
        String messaggio = modificaVeicoloResponseDTO.getMessaggio();
    }

}
