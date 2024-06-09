package com.polimi.carzone.dto.request;

import lombok.Data;
// annotazione di Lombok che genera tutti i metodi getter e setter
@Data
public class DettagliVeicoloRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private Long idVeicolo;
}
