package com.polimi.carzone.dto.request;

import lombok.Data;

@Data
public class AggiuntaVeicoloRequestDTO {

    private String targa;
    private String marca;
    private String modello;
    private int chilometraggio;
    private int annoProduzione;
    private int potenzaCv;
    private String alimentazione;
    private double prezzo;

}
