package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//annotazione di lombok per generare i metodi getter e setter
@Data
//annotazione di lombok per generare un costruttore con tutti i parametri
@AllArgsConstructor
//annotazione di lombok per generare un costruttore vuoto
@NoArgsConstructor
public class AppuntamentoModificaResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
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
