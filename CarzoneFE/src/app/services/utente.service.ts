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
//classe che si occupa di gestire le chiamate al back-end per la gestione degli utenti
export class UtenteService {
  //definizione dell'url principale del back-end per la gestione degli utenti
  private backEndUrl: string = globalBackEndUrl + 'utente/';

  //cosntruttore con il client http
  constructor(private http: HttpClient) {}

  //metodo che si occupa di registrare un utente tramite i dati passati come parametro
  registra(
    email: string,
    nome: string,
    cognome: string,
    dataNascita: Date,
    username: string,
    password: string,
    passwordRipetuta: string
  ): Observable<MessageResponse> {
    //creazione della request tramite il DTO
    const request: RegisterRequest = {
      email,
      nome,
      cognome,
      dataNascita,
      username,
      password,
      passwordRipetuta,
    };
    //chiamata al back-end per la registrazione dell'utente
    return this.http.post<MessageResponse>(this.backEndUrl + 'signup', request);
  }

  //metodo che si occupa di effettuare il login di un utente tramite i dati passati come parametro
  login(username: string, password: string): Observable<LoginResponse> {
    //creazione della request tramite il DTO
    const request: LoginRequest = {
      username,
      password,
    };
    //chiamata al back-end per il login dell'utente
    return this.http.post<LoginResponse>(this.backEndUrl + 'login', request);
  }

  //metodo che si occupa di verificare se l'utente è autenticato
  accessoEffettuato(): boolean {
    return !!localStorage.getItem('token');
  }

  //metodo che si occupa di effettuare il logout dell'utente
  logout(): void {
    localStorage.clear();
  }

  //metodo che trova tutti gli utenti per il manager
  getAllUtentiManager(): Observable<ShowUtenteManagerResponse[]> {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare tutti gli utenti
    return this.http.get<ShowUtenteManagerResponse[]>(this.backEndUrl + 'utentiManager', {headers: token});
  }

  //metodo che elimina un utente tramite l'id
  eliminaUtente(id: number): Observable<MessageResponse> {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per eliminare l'utente
    return this.http.delete<MessageResponse>(this.backEndUrl + 'elimina/' + id, {headers: token});
  }

  //metodo che registra un dipendente tramite i dati passati come parametro
  registraDipendente(
    email: string,
    nome: string,
    cognome: string,
    dataNascita: Date,
    username: string,
    password: string,
    passwordRipetuta: string
  ): Observable<MessageResponse> {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: RegisterRequest = {
      email,
      nome,
      cognome,
      dataNascita,
      username,
      password,
      passwordRipetuta,
    };
    //chiamata al back-end per registrare il dipendente
    return this.http.post<MessageResponse>(this.backEndUrl + 'registraDipendente', request, {headers: token});
  }

  //metodo che recupera un utente tramite l'id
  getUtente(id: number): Observable<ShowUtenteManagerResponse> {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare l'utente
    return this.http.get<ShowUtenteManagerResponse>(this.backEndUrl + 'trova/' + id, {headers: token});
  }

  //metodo che modifica un utente tramite i dati passati come parametro
  modificaUtente(utente: ShowUtenteManagerResponse):Observable<MessageResponse> {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per modificare l'utente
    return this.http.put<MessageResponse>(this.backEndUrl + 'modifica/' + utente.id.toString(), utente, {headers: token});
  }

  //metodo che recupera tutti i clienti
  getClienti() {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare tutti i clienti
    return this.http.get<ShowUtenteManagerResponse[]>(this.backEndUrl + 'trovaClienti', {headers: token});
  }

  //metodo che recupera tutti i dipendenti
  getDipendenti() {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare tutti i dipendenti
    return this.http.get<ShowUtenteManagerResponse[]>(this.backEndUrl + 'trovaDipendenti', {headers: token});
  }

  //metodo che recupera tutti i manager
  private recuperaToken(): HttpHeaders {
    //recupero del token per l'autenticazione
    const token = localStorage.getItem('token');
    //ritorno dell'header con il token
    return new HttpHeaders({'Authorization': 'Bearer ' + token})
  }
}
