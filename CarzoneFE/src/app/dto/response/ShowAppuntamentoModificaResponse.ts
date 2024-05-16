import {ShowUtenteManagerResponse} from "./ShowUtenteManagerResponse";
import {ShowDettagliVeicoloManagerResponse} from "./ShowDettagliVeicoloManagerResponse";

export interface ShowAppuntamentoModificaResponse {
  dataOra: Date;
  cliente: ShowUtenteManagerResponse;
  veicolo: ShowDettagliVeicoloManagerResponse;
  dipendente: ShowUtenteManagerResponse;
}
