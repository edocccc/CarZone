export interface AggiungiVeicoloRequest {
    //definizione dello standard tramite la dichiarazione delle variabili
    targa: string;
    marca: string;
    modello: string;
    chilometraggio: number;
    annoProduzione: number;
    potenzaCv: number;
    alimentazione: string;
    prezzo: number;
}
