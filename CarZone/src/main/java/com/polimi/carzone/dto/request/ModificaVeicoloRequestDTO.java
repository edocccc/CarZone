package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// annotazione lombok per generare i metodi getter
@Getter
// annotazione lombok per generare un costruttore con tutti i parametri
@AllArgsConstructor
// annotazione lombok per generare un costruttore senza parametri
@NoArgsConstructor
public class ModificaVeicoloRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
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
