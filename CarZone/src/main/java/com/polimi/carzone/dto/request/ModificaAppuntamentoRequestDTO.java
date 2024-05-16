package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ModificaAppuntamentoRequestDTO {
    private LocalDateTime dataOra;
    private Long idVeicolo;
    private Long idCliente;
    private Long idDipendente;
}
