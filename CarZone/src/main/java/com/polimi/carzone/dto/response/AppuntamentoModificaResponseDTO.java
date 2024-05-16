package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppuntamentoModificaResponseDTO {
    LocalDateTime dataOra;
    Long idCliente;
    String nomeCliente;
    String cognomeCliente;
    Long idVeicolo;
    String targaVeicolo;
    String marcaVeicolo;
    String modelloVeicolo;
    Long idDipendente;
    String nomeDipendente;
    String cognomeDipendente;
}
