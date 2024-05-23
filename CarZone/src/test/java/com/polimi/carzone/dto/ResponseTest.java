package com.polimi.carzone.dto;

import com.polimi.carzone.dto.response.AggiuntaVeicoloResponseDTO;
import com.polimi.carzone.dto.response.AppuntamentoConRecensioneResponseDTO;
import com.polimi.carzone.model.Appuntamento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class ResponseTest {
    @Test
    void responseDTO() {
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

}
