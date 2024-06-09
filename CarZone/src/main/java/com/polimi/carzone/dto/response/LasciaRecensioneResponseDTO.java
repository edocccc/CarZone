package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
//annorazione lombok per generare i metodi getter
@Getter
// annorazione lombok per generare il costruttore con tutti i parametri
@AllArgsConstructor
// annorazione lombok per generare il costruttore senza parametri
@NoArgsConstructor
public class LasciaRecensioneResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String messaggio;
}
