package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LasciaRecensioneRequestDTO {
    private Long idAppuntamento;
    private Integer votoRecensione;
    private String testoRecensione;
}

