package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LasciaRecensioneRequestDTO {
    private Long idAppuntamento;
    private Integer votoRecensione;
    private String testoRecensione;
}

