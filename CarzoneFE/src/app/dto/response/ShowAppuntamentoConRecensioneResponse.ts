export interface ShowAppuntamentoConRecensioneResponse {
  id: number;
  dataOra: Date;
  nomeCliente: string;
  cognomeCliente: string;
  nomeDipendente: string;
  cognomeDipendente: string;
  targaVeicolo: string;
  marcaVeicolo: string;
  modelloVeicolo: string;
  recensioneVoto: number;
  recensioneTesto: string;
  esitoRegistrato: boolean;
  dataPassata: boolean;
}
