package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

//annotazione lombok per generare i metodi getter
@Getter
//annotazione lombok per generare un costruttore con tutti i parametri
@AllArgsConstructor
public class RecensioneResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String nomeCliente;
    private String cognomeCliente;
    private Integer voto;
    private String testo;
}
