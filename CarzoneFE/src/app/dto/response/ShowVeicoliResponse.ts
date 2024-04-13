export interface Veicolo {
  marca: string;
  modello: string;
  prezzo: number;
}

export interface ShowVeicoliResponse {
  veicoli: Veicolo[];
}
