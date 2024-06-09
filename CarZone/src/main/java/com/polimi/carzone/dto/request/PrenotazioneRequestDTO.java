package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//annotazione di lombok per generare i metodi getter e setter
@Data
//annotazioni di lombok per generare un costruttore con tutti i parametri e un costruttore vuoto
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private LocalDateTime dataOra;
    private Long idVeicolo;
    private Long idCliente;
}
