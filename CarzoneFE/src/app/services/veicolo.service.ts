import { Injectable } from '@angular/core';
import {globalBackEndUrl} from "../../../environment";
import {map, Observable} from "rxjs";
import {ShowVeicoloResponse} from "../dto/response/ShowVeicoloResponse";
import {HttpClient} from "@angular/common/http";
import {ShowDettagliVeicoloResponse} from "../dto/response/ShowDettagliVeicoloResponse";
import {RicercaRequest} from "../dto/request/RicercaRequest";
import {ShowAppuntamentoResponse} from "../dto/response/ShowAppuntamentoResponse";
import {AppuntamentiDipendenteRequest} from "../dto/request/AppuntamentiDipendenteRequest";
import {RegistrazioneVenditaRequest} from "../dto/request/RegistrazioneVenditaRequest";
import {MessageResponse} from "../dto/response/MessageResponse";
import {ShowDettagliVeicoloManagerResponse} from "../dto/response/ShowDettagliVeicoloManagerResponse";
import {AggiungiVeicoloRequest} from "../dto/request/AggiungiVeicoloRequest";

@Injectable({
  providedIn: 'root'
})
export class VeicoloService {
  private backEndUrl: string = globalBackEndUrl + 'veicolo/';

  constructor(private http: HttpClient) { }

  getVeicoli(): Observable<ShowVeicoloResponse[]> {
    return this.http.get<ShowVeicoloResponse[]>(this.backEndUrl + 'veicoli').pipe(
      map(response => {
        console.log("inizio del service")
        console.log(response);
        console.log("fine del service")
        return response;
      })
    );
  }

  getVeicolo(id: string): Observable<ShowDettagliVeicoloResponse> {
    if(id == null || id == "" ){
        throw new Error("id non può essere nullo");
    }
    return this.http.get<ShowDettagliVeicoloResponse>(this.backEndUrl + 'dettagli/' + id);
  }

  ricerca(
    criterio: string,
    targa: string,
    marca: string,
    modello: string,
    potenzaMinima: number,
    potenzaMassima: number,
    prezzoMinimo: number,
    prezzoMassimo: number,
    alimentazione: string,
    annoProduzioneMinimo: number,
    annoProduzioneMassimo: number,
    chilometraggioMinimo: number,
    chilometraggioMassimo: number
  ): Observable<ShowVeicoloResponse[]> {
    const request: RicercaRequest = {
      criterio,
      targa,
      marca,
      modello,
      potenzaMinima,
      potenzaMassima,
      prezzoMinimo,
      prezzoMassimo,
      alimentazione,
      annoProduzioneMinimo,
      annoProduzioneMassimo,
      chilometraggioMinimo,
      chilometraggioMassimo
    };
    return this.http.post<ShowVeicoloResponse[]>(this.backEndUrl + 'cerca', request);
  }

  registraVendita(idAppuntamento: number, venditaConclusa: boolean): Observable<MessageResponse>{
    const request: RegistrazioneVenditaRequest = {
        idAppuntamento,
        venditaConclusa
    }
    return this.http.post<MessageResponse>(this.backEndUrl + 'registraVendita', request);
  }

  getAllVeicoliConDettagli(): Observable<ShowDettagliVeicoloManagerResponse[]> {
    return this.http.get<ShowDettagliVeicoloManagerResponse[]>(this.backEndUrl + 'veicoliConDettagli');
  }

  eliminaVeicolo(id: number): Observable<MessageResponse> {
    return this.http.delete<MessageResponse>(this.backEndUrl + 'elimina/' + id);
  }

  modificaVeicolo(veicolo: ShowDettagliVeicoloResponse) {
    return this.http.put<MessageResponse>(this.backEndUrl + 'modifica/' + veicolo.id.toString(), veicolo);
  }

  aggiungiVeicolo(
    targa: string,
    marca: string,
    modello: string,
    chilometraggio: number,
    annoProduzione: number,
    potenzaCv: number,
    alimentazione: string,
    prezzo: number) : Observable<MessageResponse> {
    const request: AggiungiVeicoloRequest = {
      targa,
      marca,
      modello,
      chilometraggio,
      annoProduzione,
      potenzaCv,
      alimentazione,
      prezzo
    };
    return this.http.post<MessageResponse>(this.backEndUrl + 'aggiungiVeicolo', request);
  }
}
