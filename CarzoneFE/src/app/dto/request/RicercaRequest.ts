export interface RicercaRequest {
  //definizione dello standard tramite la dichiarazione delle variabili
  criterio: string;
  targa: string;
  marca: string;
  modello: string;
  alimentazione: string;
  prezzoMinimo: number;
  prezzoMassimo: number;
  potenzaMinima: number;
  potenzaMassima: number;
  chilometraggioMinimo: number;
  chilometraggioMassimo: number;
  annoProduzioneMinimo: number;
  annoProduzioneMassimo: number;
}
