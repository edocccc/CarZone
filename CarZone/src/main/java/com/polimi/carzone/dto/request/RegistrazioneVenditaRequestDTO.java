package com.polimi.carzone.dto.request;

import lombok.Data;

@Data
public class RegistrazioneVenditaRequestDTO {
    private Long idAppuntamento;
    private boolean venditaConclusa;
}
