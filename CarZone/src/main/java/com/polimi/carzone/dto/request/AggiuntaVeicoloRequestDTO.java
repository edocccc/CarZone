package com.polimi.carzone.dto.request;

import lombok.Data;

@Data
public class AggiuntaVeicoloRequestDTO {

    private String targa;
    private String marca;
    private String modello;
    private Integer chilometraggio;
    private Integer annoProduzione;
    private Integer potenzaCv;
    private String alimentazione;
    private Double prezzo;

}
