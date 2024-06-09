package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//annotazione lombok per generare i metodi getter e setter
@Data
//annotazioni lombok per generare un costruttore con tutti i parametri e uno senza parametri
@AllArgsConstructor
// annotazione lombok per generare un costruttore senza parametri
@NoArgsConstructor
public class AppuntamentoResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private long id;
    private LocalDateTime dataOra;
    private String nomeCliente;
    private String cognomeCliente;
    private String targaVeicolo;
    private String marcaVeicolo;
    private String modelloVeicolo;
    private boolean dataPassata;
}
