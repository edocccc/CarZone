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
    return this.http.post<MessageResponse>(this.backEndUrl + 'prendiInCarico/', request);
  }
}
