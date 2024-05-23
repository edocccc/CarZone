package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModificaAppuntamentoRequestDTO {
    private LocalDateTime dataOra;
    private Long idVeicolo;
    private Long idCliente;
    private Long idDipendente;
}
