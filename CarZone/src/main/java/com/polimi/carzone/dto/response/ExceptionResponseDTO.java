package com.polimi.carzone.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

//annotazione lombok per generare i metodi getter e setter
@Data
public class ExceptionResponseDTO {

    // definizione dello standard dato dal dto in base agli attributi
    private LocalDateTime timestamp;

    private Map<String,String> errori;
}
