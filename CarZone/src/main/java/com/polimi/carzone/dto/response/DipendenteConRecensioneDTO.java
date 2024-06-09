package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

//annotazione lombok per generare automaticamente i getter
@Getter
//annotazione lombok per generare automaticamente un costruttore con tutti i parametri
@AllArgsConstructor
public class DipendenteConRecensioneDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String nomeDipendente;
    private String cognomeDipendente;
    private ValutazioneMediaResponseDTO valutazioneMedia;
    private List<RecensioneResponseDTO> recensioni;
}
