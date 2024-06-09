import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpRequest} from "@angular/common/http";
import {ShowDettagliVeicoloResponse} from "../dto/response/ShowDettagliVeicoloResponse";
import {MessageResponse} from "../dto/response/MessageResponse";
import {globalBackEndUrl} from "../../../environment";
import {PrenotazioneRequest} from "../dto/request/PrenotazioneRequest";
import {Observable} from "rxjs";
import {ShowAppuntamentoResponse} from "../dto/response/ShowAppuntamentoResponse";
import {AppuntamentiDipendenteRequest} from "../dto/request/AppuntamentiDipendenteRequest";
import {ValutazioneMediaDipendenteRequest} from "../dto/request/ValutazioneMediaDipendenteRequest";
import {ShowValutazioneMediaResponse} from "../dto/response/ShowValutazioneMediaResponse";
import {PrendiInCaricoRequest} from "../dto/request/PrendiInCaricoRequest";
import {ShowRecensioneResponse} from "../dto/response/ShowRecensioneResponse";
import {ShowValutazioniDipendenti} from "../dto/response/ShowValutazioniDipendenti";
import {ShowAppuntamentoManagerResponse} from "../dto/response/ShowAppuntamentoManagerResponse";
import {PrenotazioneManagerRequest} from "../dto/request/PrenotazioneManagerRequest";
import {ModificaAppuntamentoManagerRequest} from "../dto/request/ModificaAppuntamentoManagerRequest";
import {LasciaRecensioneRequest} from "../dto/request/LasciaRecensioneRequest";
import {ShowAppuntamentoConRecensioneResponse} from "../dto/response/ShowAppuntamentoConRecensioneResponse";
import {ShowRecensioniClienteResponse} from "../dto/response/ShowRecensioniClienteResponse";
import {ShowAppuntamentoModificaResponse} from "../dto/response/ShowAppuntamentoModificaResponse";

@Injectable({
  providedIn: 'root'
})
//classe che si occupa di gestire le chiamate al back-end per la gestione degli appuntamenti
export class AppuntamentoService {
  //url principale del back-end per la gestione degli appuntamenti
  private backEndUrl: string = globalBackEndUrl + 'appuntamento/';

  //costruttore con il client http
  constructor(private http: HttpClient) { }

  //metodo per la prenotazione di un appuntamento tramite la data, l'id del veicolo e l'id del cliente
  prenota(data: Date, idVeicolo: number, idCliente : number) {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: PrenotazioneRequest = {
      dataOra: data,
      idVeicolo: idVeicolo,
      idCliente: idCliente
    }

    //chiamata al back-end per la prenotazione dell'appuntamento
    return this.http.post<MessageResponse>(this.backEndUrl + 'prenota', request, {headers: token});
  }

  //metodo per recuperare gli appuntamenti di un dipendente tramite il suo id
  getAppuntamentiDipendente(idDipendente: number): Observable<ShowAppuntamentoResponse[]> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: AppuntamentiDipendenteRequest = {
      idDipendente
    };
    //chiamata al back-end per recuperare gli appuntamenti del dipendente
    return this.http.get<ShowAppuntamentoResponse[]>(this.backEndUrl + 'dipendente/'+ request.idDipendente, {headers: token});
  }

  //metodo per recuperare la valutazione media di un dipendente tramite il suo id
  getValutazioneMedia(idDipendente: number): Observable<ShowValutazioneMediaResponse>{
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: ValutazioneMediaDipendenteRequest = {
      idDipendente
    };
    //chiamata al back-end per recuperare la valutazione media del dipendente
    return this.http.get<ShowValutazioneMediaResponse>(this.backEndUrl + 'dipendente/' + request.idDipendente + '/valutazione', {headers: token});

  }

  //metodo per recuperare gli appuntamenti liberi
  getAppuntamentiLiberi(): Observable<ShowAppuntamentoResponse[]> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare gli appuntamenti liberi
    return this.http.get<ShowAppuntamentoResponse[]>(this.backEndUrl + 'appuntamentiLiberi', {headers: token});
  }

  //metodo per prendere in carico un appuntamento tramite l'id del dipendente e l'id dell'appuntamento
  prendiInCarico(idDipendente: number, idAppuntamento: number): Observable<MessageResponse> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: PrendiInCaricoRequest = {
      idDipendente,
      idAppuntamento
    }
    //chiamata al back-end per prendere in carico l'appuntamento
    return this.http.post<MessageResponse>(this.backEndUrl + 'prendiInCarico', request, {headers: token});
  }

  //metodo per recuperare le recensioni di un dipendente tramite il suo id
  getRecensioniDipendente(idDipendente: number): Observable<ShowRecensioneResponse[]> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare le recensioni del dipendente
    return this.http.get<ShowRecensioneResponse[]>(this.backEndUrl + 'recensioni/' + idDipendente, {headers: token});
  }

  //metodo per recuperare i dipendenti con le recensioni
  getDipendentiConRecensioni(): Observable<ShowValutazioniDipendenti[]> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare i dipendenti con le recensioni
    return this.http.get<ShowValutazioniDipendenti[]>(this.backEndUrl + 'dipendentiConRecensioni', {headers: token});
  }

  //metodo per recuperare tutti gli appuntamenti per un manager
  getAllAppuntamentiManager(): Observable<ShowAppuntamentoManagerResponse[]> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare tutti gli appuntamenti del manager
    return this.http.get<ShowAppuntamentoManagerResponse[]>(this.backEndUrl + 'trovaPerManager', {headers: token});
  }

  //metodo per eliminare un appuntamento tramite il suo id
  eliminaAppuntamento(id: number) {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per eliminare l'appuntamento
    return this.http.delete<MessageResponse>(this.backEndUrl + 'elimina/' + id, {headers: token});
  }

  //metodo per prenotare un appuntamento tramite la data, l'id del veicolo, l'id del cliente e l'id del dipendente
  prenotaAppuntamento(dataOra: Date, idVeicolo: number, idCliente: number, idDipendente: number) {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: PrenotazioneManagerRequest = {
      dataOra,
      idVeicolo,
      idCliente,
      idDipendente
    }
    //chiamata al back-end per prenotare l'appuntamento
    return this.http.post<MessageResponse>(this.backEndUrl + 'prenotaPerManager', request, {headers: token});
  }

  //metodo per modificare un appuntamento tramite l'id dell'appuntamento, la data, l'id del veicolo, l'id del cliente e l'id del dipendente
  modificaAppuntamento(idAppuntamento: number, dataOra: Date, idVeicolo: number, idCliente: number, idDipendente: number) {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: ModificaAppuntamentoManagerRequest = {
      dataOra,
      idVeicolo,
      idCliente,
      idDipendente
    }
    //chiamata al back-end per modificare l'appuntamento
    return this.http.put<MessageResponse>(this.backEndUrl + 'modifica/' + idAppuntamento, request, {headers: token});
  }

  //metodo per recuperare tutti gli appuntamenti di un cliente tramite il suo id
  getAllAppuntamentiCliente(idCliente: number): Observable<ShowAppuntamentoConRecensioneResponse[]> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare tutti gli appuntamenti del cliente
    return this.http.get<ShowAppuntamentoConRecensioneResponse[]>(this.backEndUrl + 'appuntamentiCliente/' + idCliente, {headers: token});
  }

  //metodo per inviare una recensione tramite l'id dell'appuntamento, il voto e il testo della recensione
  inviaRecensione(idAppuntamento: number, votoRecensione: number, testoRecensione: string):Observable<MessageResponse> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: LasciaRecensioneRequest = {
      idAppuntamento,
      votoRecensione,
      testoRecensione
    }
    //chiamata al back-end per inviare la recensione
    return this.http.post<MessageResponse>(this.backEndUrl + 'lasciaRecensione', request, {headers: token});
  }

  //metodo per recuperare tutte le recensioni di un cliente tramite il suo id
  getAllRecensioniCliente(idCliente: number): Observable<ShowRecensioniClienteResponse[]> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare tutte le recensioni del cliente
    return this.http.get<ShowRecensioniClienteResponse[]>(this.backEndUrl + 'recensioniCliente/' + idCliente.toString(), {headers: token} );
  }

  //metodo per recuperare un appuntamento tramite il suo id
  getAppuntamento(idAppuntamento: number): Observable<ShowAppuntamentoModificaResponse> {
    //recupero del token di autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per recuperare l'appuntamento
    return this.http.get<ShowAppuntamentoModificaResponse>(this.backEndUrl + 'trovaPerModifica/' + idAppuntamento, {headers: token});
  }

  //metodo per recuperare il token di un utente
  private recuperaToken(): HttpHeaders {
    //recupero del token di autenticazione
    const token = localStorage.getItem('token');
    //ritorno del token
    return new HttpHeaders({'Authorization': 'Bearer ' + token})
  }
}
