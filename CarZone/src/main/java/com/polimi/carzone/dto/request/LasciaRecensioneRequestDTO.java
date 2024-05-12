package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LasciaRecensioneRequestDTO {
    private Integer idAppuntamento;
    private Integer votoRecensione;
    private String testoRecensione;
}

