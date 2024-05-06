package com.polimi.carzone.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppuntamentoResponseDTO {
    private long id;
    private LocalDateTime dataOra;
    private String nomeCliente;
    private String cognomeCliente;
    private String targaVeicolo;
    private String marcaVeicolo;
    private String modelloVeicolo;
    private boolean dataPassata;
}
