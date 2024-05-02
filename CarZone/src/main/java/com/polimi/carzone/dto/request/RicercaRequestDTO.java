package com.polimi.carzone.dto.request;

import lombok.Data;

@Data
public class RicercaRequestDTO {
    private String criterio;
    private String targa;
    private String marca;
    private String modello;
    private String alimentazione;
    private Double prezzoMinimo;
    private Double prezzoMassimo;
    private Integer potenzaMinima;
    private Integer potenzaMassima;
    private Integer chilometraggioMinimo;
    private Integer chilometraggioMassimo;
    private Integer annoProduzioneMinimo;
    private Integer annoProduzioneMassimo;
}
