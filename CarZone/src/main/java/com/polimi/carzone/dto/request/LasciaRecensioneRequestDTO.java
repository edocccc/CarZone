package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// annotazione lombok per generare automaticamente i metodi getter
@Getter
// annotazione lombok per generare automaticamente un costruttore con tutti i parametri
@AllArgsConstructor
// annotazione lombok per generare automaticamente un costruttore vuoto
@NoArgsConstructor
public class LasciaRecensioneRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private Long idAppuntamento;
    private Integer votoRecensione;
    private String testoRecensione;
}

