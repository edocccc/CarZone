import { Injectable } from '@angular/core';
import { globalBackEndUrl } from 'environment';
import { HttpClient } from '@angular/common/http';
import { RegisterRequest } from '../dto/request/RegisterRequest';
import { LoginRequest } from '../dto/request/LoginRequest';
import { Observable } from 'rxjs';
import { MessageResponse } from '../dto/response/MessageResponse';
import {LoginResponse} from "../dto/response/LoginResponse";

@Injectable({
  providedIn: 'root',
})
export class UtenteService {
  private backEndUrl: string = globalBackEndUrl + 'utente/';

  constructor(private http: HttpClient) {}

  registra(
    email: string,
    nome: string,
    cognome: string,
    dataNascita: Date,
    username: string,
    password: string,
    passwordRipetuta: string
  ): Observable<MessageResponse> {
    const request: RegisterRequest = {
      email,
      nome,
      cognome,
      dataNascita,
      username,
      password,
      passwordRipetuta,
    };
    return this.http.post<MessageResponse>(this.backEndUrl + 'signup', request);
  }

  login(username: string, password: string): Observable<LoginResponse> {
    const request: LoginRequest = {
      username,
      password,
    };
    return this.http.post<LoginResponse>(this.backEndUrl + 'login', request);
  }
}
