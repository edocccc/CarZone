package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

//annotazione lombok per generare automaticamente i metodi getter e setter
@Data
//annotazione lombok per generare automaticamente il costruttore con tutti i parametri
@AllArgsConstructor
//annotazione lombok per generare automaticamente il costruttore senza parametri
@NoArgsConstructor
public class AppuntamentoManagerResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private long id;
    private LocalDateTime dataOra;
    private String nomeCliente;
    private String cognomeCliente;
    private String nomeDipendente;
    private String cognomeDipendente;
    private String targaVeicolo;
    private String marcaVeicolo;
    private String modelloVeicolo;
    private boolean esitoRegistrato;
    private boolean dataPassata;
}
