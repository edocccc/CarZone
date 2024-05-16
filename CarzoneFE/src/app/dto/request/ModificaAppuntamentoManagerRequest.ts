import {ShowAppuntamentoModificaResponse} from "../response/ShowAppuntamentoModificaResponse";

export interface ModificaAppuntamentoManagerRequest {
    dataOra: Date;
    idVeicolo: number;
    idCliente: number;
    idDipendente: number;
}
