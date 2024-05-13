import { Injectable } from '@angular/core';
import { globalBackEndUrl } from 'environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { RegisterRequest } from '../dto/request/RegisterRequest';
import { LoginRequest } from '../dto/request/LoginRequest';
import {BehaviorSubject, Observable} from 'rxjs';
import { MessageResponse } from '../dto/response/MessageResponse';
import {LoginResponse} from "../dto/response/LoginResponse";
import {ShowUtenteManagerResponse} from "../dto/response/ShowUtenteManagerResponse";
import {ShowDettagliVeicoloResponse} from "../dto/response/ShowDettagliVeicoloResponse";

@Injectable({
  providedIn: 'root',
})
export class UtenteService {
  private backEndUrl: string = globalBackEndUrl + 'utente/';
  private isAuthenticatedSource = new BehaviorSubject<boolean>(this.checkIsAuthenticatedInitial());
  isAuthenticated$ = this.isAuthenticatedSource.asObservable();

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
    const token: HttpHeaders = this.recuperaToken();
    const request: RegisterRequest = {
      email,
      nome,
      cognome,
      dataNascita,
      username,
      password,
      passwordRipetuta,
    };
    return this.http.post<MessageResponse>(this.backEndUrl + 'signup', request, {headers: token});
  }

  login(username: string, password: string): Observable<LoginResponse> {
    const token: HttpHeaders = this.recuperaToken();
    const request: LoginRequest = {
      username,
      password,
    };
    this.setIsAuthenticated(true);
    return this.http.post<LoginResponse>(this.backEndUrl + 'login', request, {headers: token});
  }

  accessoEffettuato(): boolean {
    return !!localStorage.getItem('token');
  }

  logout(): void {
    this.setIsAuthenticated(false);
    localStorage.clear();
  }

  private checkIsAuthenticatedInitial(): boolean {
    const token = localStorage.getItem('token');
    return !!token; // Restituisce true se token esiste, altrimenti false
  }

  // Usato per accedere allo stato corrente in un modo reattivo
  checkIsAuthenticated(): Observable<boolean> {
    return this.isAuthenticated$;
  }

  setIsAuthenticated(value: boolean) {
    this.isAuthenticatedSource.next(value);
  }

  getAllUtentiManager(): Observable<ShowUtenteManagerResponse[]> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowUtenteManagerResponse[]>(this.backEndUrl + 'utentiManager', {headers: token});
  }

  eliminaUtente(id: number): Observable<MessageResponse> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.delete<MessageResponse>(this.backEndUrl + 'elimina/' + id, {headers: token});
  }

  registraDipendente(
    email: string,
    nome: string,
    cognome: string,
    dataNascita: Date,
    username: string,
    password: string,
    passwordRipetuta: string
  ): Observable<MessageResponse> {
    const token: HttpHeaders = this.recuperaToken();
    const request: RegisterRequest = {
      email,
      nome,
      cognome,
      dataNascita,
      username,
      password,
      passwordRipetuta,
    };
    return this.http.post<MessageResponse>(this.backEndUrl + 'registraDipendente', request, {headers: token});
  }

  getUtente(id: number): Observable<ShowUtenteManagerResponse> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowUtenteManagerResponse>(this.backEndUrl + 'trova/' + id, {headers: token});
  }

  modificaUtente(utente: ShowUtenteManagerResponse):Observable<MessageResponse> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.put<MessageResponse>(this.backEndUrl + 'modifica/' + utente.id.toString(), utente, {headers: token});
  }

  getClienti() {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowUtenteManagerResponse[]>(this.backEndUrl + 'trovaClienti', {headers: token});
  }

  getDipendenti() {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowUtenteManagerResponse[]>(this.backEndUrl + 'trovaDipendenti', {headers: token});
  }

  private recuperaToken(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({'Authorization': 'Bearer ' + token})
  }
}
