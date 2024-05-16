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
export class AppuntamentoService {
  private backEndUrl: string = globalBackEndUrl + 'appuntamento/';

  constructor(private http: HttpClient) { }

  prenota(data: Date, idVeicolo: number, idCliente : number) {
    const token: HttpHeaders = this.recuperaToken();
    const request: PrenotazioneRequest = {
      dataOra: data,
      idVeicolo: idVeicolo,
      idCliente: idCliente
    }

    return this.http.post<MessageResponse>(this.backEndUrl + 'prenota', request, {headers: token});
  }

  getAppuntamentiDipendente(idDipendente: number): Observable<ShowAppuntamentoResponse[]> {
    const token: HttpHeaders = this.recuperaToken();
    const request: AppuntamentiDipendenteRequest = {
      idDipendente
    };
    return this.http.get<ShowAppuntamentoResponse[]>(this.backEndUrl + 'dipendente/'+ request.idDipendente, {headers: token});
  }

  getValutazioneMedia(idDipendente: number): Observable<ShowValutazioneMediaResponse>{
    const token: HttpHeaders = this.recuperaToken();
    const request: ValutazioneMediaDipendenteRequest = {
      idDipendente
    };
    return this.http.get<ShowValutazioneMediaResponse>(this.backEndUrl + 'dipendente/' + request.idDipendente + '/valutazione', {headers: token});

  }

  getAppuntamentiLiberi(): Observable<ShowAppuntamentoResponse[]> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowAppuntamentoResponse[]>(this.backEndUrl + 'appuntamentiLiberi', {headers: token});
  }

  prendiInCarico(idDipendente: number, idAppuntamento: number): Observable<MessageResponse> {
    const token: HttpHeaders = this.recuperaToken();
    const request: PrendiInCaricoRequest = {
      idDipendente,
      idAppuntamento
    }
    return this.http.post<MessageResponse>(this.backEndUrl + 'prendiInCarico', request, {headers: token});
  }

  getRecensioniDipendente(idDipendente: number): Observable<ShowRecensioneResponse[]> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowRecensioneResponse[]>(this.backEndUrl + 'recensioni/' + idDipendente, {headers: token});
  }

  getDipendentiConRecensioni(): Observable<ShowValutazioniDipendenti[]> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowValutazioniDipendenti[]>(this.backEndUrl + 'dipendentiConRecensioni', {headers: token});
  }

  getAllAppuntamentiManager(): Observable<ShowAppuntamentoManagerResponse[]> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowAppuntamentoManagerResponse[]>(this.backEndUrl + 'trovaPerManager', {headers: token});
  }

  eliminaAppuntamento(id: number) {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.delete<MessageResponse>(this.backEndUrl + 'elimina/' + id, {headers: token});
  }

  prenotaAppuntamento(dataOra: Date, idVeicolo: number, idCliente: number, idDipendente: number) {
    const token: HttpHeaders = this.recuperaToken();
    const request: PrenotazioneManagerRequest = {
      dataOra,
      idVeicolo,
      idCliente,
      idDipendente
    }
    return this.http.post<MessageResponse>(this.backEndUrl + 'prenotaPerManager', request, {headers: token});
  }

  modificaAppuntamento(idAppuntamento: number, dataOra: Date, idVeicolo: number, idCliente: number, idDipendente: number) {
    const token: HttpHeaders = this.recuperaToken();
    const request: ModificaAppuntamentoManagerRequest = {
      dataOra,
      idVeicolo,
      idCliente,
      idDipendente
    }
    return this.http.put<MessageResponse>(this.backEndUrl + 'modifica/' + idAppuntamento, request, {headers: token});
  }

  getAllAppuntamentiCliente(idCliente: number): Observable<ShowAppuntamentoConRecensioneResponse[]> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowAppuntamentoConRecensioneResponse[]>(this.backEndUrl + 'appuntamentiCliente/' + idCliente.toString(), {headers: token});
  }

  inviaRecensione(idAppuntamento: number, votoRecensione: number, testoRecensione: string):Observable<MessageResponse> {
    const token: HttpHeaders = this.recuperaToken();
    const request: LasciaRecensioneRequest = {
      idAppuntamento,
      votoRecensione,
      testoRecensione
    }
    return this.http.post<MessageResponse>(this.backEndUrl + 'lasciaRecensione', request, {headers: token});
  }

  getAllRecensioniCliente(idCliente: number): Observable<ShowRecensioniClienteResponse[]> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowRecensioniClienteResponse[]>(this.backEndUrl + 'recensioniCliente/' + idCliente.toString(), {headers: token} );
  }

  getAppuntamento(idAppuntamento: number): Observable<ShowAppuntamentoModificaResponse> {
    const token: HttpHeaders = this.recuperaToken();
    return this.http.get<ShowAppuntamentoModificaResponse>(this.backEndUrl + 'trovaPerModifica/' + idAppuntamento, {headers: token});
  }

  private recuperaToken(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({'Authorization': 'Bearer ' + token})
  }
}
