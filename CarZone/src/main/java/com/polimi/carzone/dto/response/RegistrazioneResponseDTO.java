package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

//annotazione lombok per generare i metodi getter
@Getter
//annotazione lombok per generare un costruttore con tutti i campi della classe
@AllArgsConstructor
public class RegistrazioneResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String messaggio;
}
