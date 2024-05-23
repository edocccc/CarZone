package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneRequestDTO {
    private LocalDateTime dataOra;
    private Long idVeicolo;
    private Long idCliente;
}
