package com.polimi.carzone.dto.request;

import lombok.Data;

//annotazione di Lombok per generare costruttori, metodi getter e setter
@Data
public class RegistrazioneVenditaRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private Long idAppuntamento;
    private boolean venditaConclusa;
}
