package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecensioneClienteResponseDTO {
    private String nomeDipendente;
    private String cognomeDipendente;
    private int recensioneVoto;
    private String recensioneTesto;
}
