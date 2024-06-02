export interface ShowDettagliVeicoloResponse {
  id: number;
  targa: string;
  marca: string;
  modello: string;
  chilometraggio: number;
  annoProduzione: number;
  potenzaCv: number;
  alimentazione: string;
  prezzo: number;
  immagine: Blob;
}
