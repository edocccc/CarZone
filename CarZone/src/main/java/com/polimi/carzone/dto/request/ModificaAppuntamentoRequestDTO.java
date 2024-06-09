package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//annotazione di lombok per generare automaticamente i metodi Getter
@Getter
//annotazione di lombok per generare automaticamente un costruttore con tutti i parametri
@AllArgsConstructor
//annotazione di lombok per generare automaticamente un costruttore senza parametri
@NoArgsConstructor
public class ModificaAppuntamentoRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private LocalDateTime dataOra;
    private Long idVeicolo;
    private Long idCliente;
    private Long idDipendente;
}
