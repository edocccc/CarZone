import {ShowUtenteManagerResponse} from "./ShowUtenteManagerResponse";
import {ShowDettagliVeicoloManagerResponse} from "./ShowDettagliVeicoloManagerResponse";

export interface ShowAppuntamentoModificaResponse {
  //definizione dello standard tramite la dichiarazione delle variabili
  dataOra: Date;
  idCliente: number;
  nomeCliente: string;
  cognomeCliente: string;
  idVeicolo: number;
  targaVeicolo: string;
  marcaVeicolo: string;
  modelloVeicolo: string;
  idDipendente: number;
  nomeDipendente: string;
  cognomeDipendente: string;
}
