package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PrenotazioneRequestDTO {
    private LocalDateTime dataOra;
    private long idVeicolo;
    private long idCliente;
}
