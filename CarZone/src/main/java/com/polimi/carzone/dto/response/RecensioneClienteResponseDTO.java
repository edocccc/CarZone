package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//annotazione lombok per generare automaticamente i metodi getter e setter
@Data
// annotazione lombok per generare automaticamente un costruttore con tutti i parametri
@AllArgsConstructor
// annotazione lombok per generare automaticamente un costruttore senza parametri
@NoArgsConstructor
public class RecensioneClienteResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String nomeDipendente;
    private String cognomeDipendente;
    private int recensioneVoto;
    private String recensioneTesto;
}
