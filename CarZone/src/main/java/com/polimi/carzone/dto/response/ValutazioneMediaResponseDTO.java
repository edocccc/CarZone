package com.polimi.carzone.dto.response;

import lombok.Data;

//annotazione di Lombok per generare i getter e setter
@Data
public class ValutazioneMediaResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private double valutazioneMedia;
}
