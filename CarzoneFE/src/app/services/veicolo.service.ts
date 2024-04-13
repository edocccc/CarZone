import { Injectable } from '@angular/core';
import {globalBackEndUrl} from "../../../environment";
import {map, Observable} from "rxjs";
import {ShowVeicoliResponse} from "../dto/response/ShowVeicoliResponse";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class VeicoloService {
  private backEndUrl: string = globalBackEndUrl + 'veicolo/';

  constructor(private http: HttpClient) { }

  getVeicoli(): Observable<ShowVeicoliResponse> {
    return this.http.get<ShowVeicoliResponse>(this.backEndUrl + 'veicoli').pipe(
      map(response => {
        return {
          veicoli: response.veicoli.map(veicolo => ({
            marca: veicolo.marca,
            modello: veicolo.modello,
            prezzo: veicolo.prezzo
          }))
        };
      })
    );
  }
}
