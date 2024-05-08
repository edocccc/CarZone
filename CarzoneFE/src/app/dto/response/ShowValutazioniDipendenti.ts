import {ShowRecensioneResponse} from "./ShowRecensioneResponse";
import {ShowValutazioneMediaResponse} from "./ShowValutazioneMediaResponse";

export interface ShowValutazioniDipendenti {
  nomeDipendente: string;
  cognomeDipendente: string;
  valutazioneMedia: ShowValutazioneMediaResponse;
  recensioni: ShowRecensioneResponse[];
}
