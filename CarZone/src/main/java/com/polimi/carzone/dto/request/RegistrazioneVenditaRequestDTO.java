package com.polimi.carzone.dto.request;

import lombok.Data;

@Data
public class RegistrazioneVenditaRequestDTO {
    private long idAppuntamento;
    private boolean venditaConclusa;
}
