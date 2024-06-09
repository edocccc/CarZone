package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

//annotazione di lombok per generare i metodi getter
@Getter
//annotazione di lombok per generare un costruttore con tutti campi della classe
@AllArgsConstructor
public class EliminaUtenteResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String messaggio;
}
