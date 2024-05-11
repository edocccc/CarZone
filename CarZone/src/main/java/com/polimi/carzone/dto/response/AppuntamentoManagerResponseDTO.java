package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class AppuntamentoManagerResponseDTO {
    private long id;
    private LocalDateTime dataOra;
    private String nomeCliente;
    private String cognomeCliente;
    private String nomeDipendente;
    private String cognomeDipendente;
    private String targaVeicolo;
    private String marcaVeicolo;
    private String modelloVeicolo;
    private boolean esitoRegistrato;
    private boolean dataPassata;
}
