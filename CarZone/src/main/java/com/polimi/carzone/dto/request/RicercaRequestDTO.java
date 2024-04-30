package com.polimi.carzone.dto.request;

import lombok.Data;

@Data
public class RicercaRequestDTO {
    private String targa;
    private String marca;
    private String modello;
    private String alimentazione;
    private int annoImmatricolazione;
    private double prezzoMinimo;
    private double prezzoMassimo;
    private int potenzaMinima;
    private int potenzaMassima;
    private int chilometraggioMinimo;
    private int chilometraggioMassimo;
    private int annoProduzioneMinimo;
    private int annoProduzioneMassimo;
}
