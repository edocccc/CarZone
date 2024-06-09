export interface LoginResponse {
  //definizione dello standard tramite la dichiarazione delle variabili
  id: number;
  email: string;
  nome: string;
  cognome: string;
  username: string;
  dataNascita: Date;
  ruolo: string;
  token: string;
}
