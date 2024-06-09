package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// annotazione lombok per generare i getter e setter
@Data
// annotazioni lombok per generare un costruttore con tutti i parametri e uno senza parametri
@AllArgsConstructor
@NoArgsConstructor
public class AggiuntaVeicoloRequestDTO {

    // dichiarazione dello standard dato dal DTO tramite gli attributi
    private String targa;
    private String marca;
    private String modello;
    private Integer chilometraggio;
    private Integer annoProduzione;
    private Integer potenzaCv;
    private String alimentazione;
    private Double prezzo;

}
