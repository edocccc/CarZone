import { Injectable } from '@angular/core';
import {HttpClient, HttpRequest} from "@angular/common/http";
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

@Injectable({
  providedIn: 'root'
})
export class AppuntamentoService {
  private backEndUrl: string = globalBackEndUrl + 'appuntamento/';

  constructor(private http: HttpClient) { }

  prenota(data: Date, idVeicolo: number, idCliente : number) {
    const request: PrenotazioneRequest = {
      dataOra: data,
      idVeicolo: idVeicolo,
      idCliente: idCliente
    }

    return this.http.post<MessageResponse>(this.backEndUrl + 'prenota', request);
  }

  getAppuntamentiDipendente(idDipendente: number): Observable<ShowAppuntamentoResponse[]> {
    const request: AppuntamentiDipendenteRequest = {
      idDipendente
    };
    return this.http.get<ShowAppuntamentoResponse[]>(this.backEndUrl + 'dipendente/'+ request.idDipendente);
  }

  getValutazioneMedia(idDipendente: number): Observable<ShowValutazioneMediaResponse>{
    const request: ValutazioneMediaDipendenteRequest = {
      idDipendente
    };
    return this.http.get<ShowValutazioneMediaResponse>(this.backEndUrl + 'dipendente/' + request.idDipendente + '/valutazione');

  }

  getAppuntamentiLiberi(): Observable<ShowAppuntamentoResponse[]> {
    return this.http.get<ShowAppuntamentoResponse[]>(this.backEndUrl + 'appuntamentiLiberi');
  }

  prendiInCarico(idDipendente: number, idAppuntamento: number): Observable<MessageResponse> {
    const request: PrendiInCaricoRequest = {
      idDipendente,
      idAppuntamento
    }
    return this.http.post<MessageResponse>(this.backEndUrl + 'prendiInCarico', request);
  }

  getRecensioniDipendente(idDipendente: number): Observable<ShowRecensioneResponse[]> {
    return this.http.get<ShowRecensioneResponse[]>(this.backEndUrl + 'recensioni/' + idDipendente);
  }

  getDipendentiConRecensioni(): Observable<ShowValutazioniDipendenti[]> {
    return this.http.get<ShowValutazioniDipendenti[]>(this.backEndUrl + 'dipendentiConRecensioni');
  }

  getAllAppuntamentiManager(): Observable<ShowAppuntamentoManagerResponse[]> {
    return this.http.get<ShowAppuntamentoManagerResponse[]>(this.backEndUrl + 'trovaPerManager');
  }

  eliminaAppuntamento(id: number) {
    return this.http.delete<MessageResponse>(this.backEndUrl + 'elimina/' + id);
  }

  prenotaAppuntamento(dataOra: Date, idVeicolo: number, idCliente: number, idDipendente: number) {
    const request: PrenotazioneManagerRequest = {
      dataOra,
      idVeicolo,
      idCliente,
      idDipendente
    }
    return this.http.post<MessageResponse>(this.backEndUrl + 'prenotaPerManager', request);
  }

  modificaAppuntamento(idAppuntamento: number, dataOra: Date, idVeicolo: number, idCliente: number, idDipendente: number) {
    const request: ModificaAppuntamentoManagerRequest = {
      idAppuntamento,
      dataOra,
      idVeicolo,
      idCliente,
      idDipendente
    }
    return this.http.put<MessageResponse>(this.backEndUrl + 'modifica/' + idAppuntamento, request);
  }

  getAllAppuntamentiCliente(idCliente: number): Observable<ShowAppuntamentoConRecensioneResponse[]> {
    return this.http.get<ShowAppuntamentoConRecensioneResponse[]>(this.backEndUrl + 'appuntamentiCliente/' + idCliente.toString());
  }

  inviaRecensione(idAppuntamento: number, votoRecensione: number, testoRecensione: string):Observable<MessageResponse> {
    const request: LasciaRecensioneRequest = {
      idAppuntamento,
      votoRecensione,
      testoRecensione
    }
    return this.http.post<MessageResponse>(this.backEndUrl + 'lasciaRecensione', request);
  }

  getAllRecensioniCliente(idCliente: number): Observable<ShowRecensioniClienteResponse[]> {
    return this.http.get<ShowRecensioniClienteResponse[]>(this.backEndUrl + 'recensioniCliente/' + idCliente.toString());
  }
}
