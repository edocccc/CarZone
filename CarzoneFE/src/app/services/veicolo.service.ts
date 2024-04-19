import { Injectable } from '@angular/core';
import {globalBackEndUrl} from "../../../environment";
import {map, Observable} from "rxjs";
import {ShowVeicoloResponse} from "../dto/response/ShowVeicoloResponse";
import {HttpClient} from "@angular/common/http";
import {ShowDettagliVeicoloResponse} from "../dto/response/ShowDettagliVeicoloResponse";

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

}
