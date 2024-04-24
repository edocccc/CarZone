import { Injectable } from '@angular/core';
import {HttpClient, HttpRequest} from "@angular/common/http";
import {ShowDettagliVeicoloResponse} from "../dto/response/ShowDettagliVeicoloResponse";
import {MessageResponse} from "../dto/response/MessageResponse";
import {globalBackEndUrl} from "../../../environment";
import {PrenotazioneRequest} from "../dto/request/PrenotazioneRequest";

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
}
