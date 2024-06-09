export interface ShowAppuntamentoManagerResponse {
  //definizione dello standard tramite la dichiarazione delle variabili
    id: number;
    dataOra: Date;
    nomeCliente: string;
    cognomeCliente: string;
    nomeDipendente: string;
    cognomeDipendente: string;
    targaVeicolo: string;
    marcaVeicolo: string;
    modelloVeicolo: string;
    esitoRegistrato: boolean;
    dataPassata: boolean;
}
