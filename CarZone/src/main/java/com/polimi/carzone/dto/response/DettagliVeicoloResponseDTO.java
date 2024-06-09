package com.polimi.carzone.dto.response;

import com.polimi.carzone.model.Alimentazione;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//annotazione lombok per generare automaticamente i metodi getter e setter
@Data
//annotazione lombok per generare automaticamente un costruttore con tutti i parametri
@AllArgsConstructor
//annotazione lombok per generare automaticamente un costruttore vuoto
@NoArgsConstructor
public class DettagliVeicoloResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
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
