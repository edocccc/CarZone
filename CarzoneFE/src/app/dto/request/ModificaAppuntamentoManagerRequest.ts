import {ShowAppuntamentoModificaResponse} from "../response/ShowAppuntamentoModificaResponse";

export interface ModificaAppuntamentoManagerRequest {
  //definizione dello standard tramite la dichiarazione delle variabili
    dataOra: Date;
    idVeicolo: number;
    idCliente: number;
    idDipendente: number;
}
