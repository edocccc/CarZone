package com.polimi.carzone.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

//annotazione lombok per generare i metodi getter e setter
@Data
public class PresaInCaricoResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String message;

}
