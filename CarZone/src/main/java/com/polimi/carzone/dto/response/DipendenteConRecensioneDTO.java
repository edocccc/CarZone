package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DipendenteConRecensioneDTO {
    private String nomeDipendente;
    private String cognomeDipendente;
    private ValutazioneMediaResponseDTO valutazioneMedia;
    private List<RecensioneResponseDTO> recensioni;
}
