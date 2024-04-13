export interface RegisterRequest {
  email: string;
  nome: string;
  cognome: string;
  dataNascita: Date;
  username: string;
  password: string;
  passwordRipetuta: string;
}
