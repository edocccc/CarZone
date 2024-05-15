package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModificaVeicoloRequestDTO {
    private Long id;
    private String targa;
    private String marca;
    private String modello;
    private Integer chilometraggio;
    private Integer annoProduzione;
    private Integer potenzaCv;
    private String alimentazione;
    private Double prezzo;

}
