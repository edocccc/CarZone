import {ShowRecensioneResponse} from "./ShowRecensioneResponse";
import {ShowValutazioneMediaResponse} from "./ShowValutazioneMediaResponse";

export interface ShowValutazioniDipendenti {
  //definizione dello standard tramite la dichiarazione delle variabili
  nomeDipendente: string;
  cognomeDipendente: string;
  valutazioneMedia: ShowValutazioneMediaResponse;
  recensioni: ShowRecensioneResponse[];
}
