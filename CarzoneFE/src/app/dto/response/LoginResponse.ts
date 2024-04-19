export interface LoginResponse {
  id: number;
  email: string;
  nome: string;
  cognome: string;
  username: string;
  dataNascita: Date;
  ruolo: string;
  token: string;
}
