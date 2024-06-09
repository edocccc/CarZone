export interface RegisterRequest {
  //definizione dello standard tramite la dichiarazione delle variabili
  email: string;
  nome: string;
  cognome: string;
  dataNascita: Date;
  username: string;
  password: string;
  passwordRipetuta: string;
}
