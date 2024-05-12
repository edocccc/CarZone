package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppuntamentoConRecensioneResponseDTO {
    private long id;
    private LocalDateTime dataOra;
    private String nomeCliente;
    private String cognomeCliente;
    private String nomeDipendente;
    private String cognomeDipendente;
    private String targaVeicolo;
    private String marcaVeicolo;
    private String modelloVeicolo;
    private int recensioneVoto;
    private String recensioneTesto;
    private boolean esitoRegistrato;
    private boolean dataPassata;
}
