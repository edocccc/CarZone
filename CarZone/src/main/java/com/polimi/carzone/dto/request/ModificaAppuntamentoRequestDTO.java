package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ModificaAppuntamentoRequestDTO {
    private long idAppuntamento;
    private LocalDateTime dataOra;
    private long idVeicolo;
    private long idCliente;
    private long idDipendente;
}
