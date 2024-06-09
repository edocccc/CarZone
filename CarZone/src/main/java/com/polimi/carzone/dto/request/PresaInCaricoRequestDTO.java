package com.polimi.carzone.dto.request;

import lombok.Data;

//annotazione di Lombok per generare tutti i metodi getter, setter
@Data
public class PresaInCaricoRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private Long idDipendente;
    private Long idAppuntamento;
}
