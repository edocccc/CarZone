export interface ShowDettagliVeicoloResponse {
  //definizione dello standard tramite la dichiarazione delle variabili
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
