export interface ShowAppuntamentoResponse {
  //definizione dello standard tramite la dichiarazione delle variabili
  id: number;
  dataOra: Date;
  nomeCliente: string;
  cognomeCliente: string;
  targaVeicolo: string;
  marcaVeicolo: string;
  modelloVeicolo: string;
  dataPassata: boolean;

}
