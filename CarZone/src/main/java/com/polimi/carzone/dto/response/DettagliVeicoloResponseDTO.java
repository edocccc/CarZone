package com.polimi.carzone.dto.response;

import com.polimi.carzone.model.Alimentazione;
import lombok.Data;

@Data
public class DettagliVeicoloResponseDTO {
    private long id;
    private String targa;
    private String marca;
    private String modello;
    private int chilometraggio;
    private int annoProduzione;
    private int potenzaCv;
    private Alimentazione alimentazione;
    private double prezzo;

}
