import { Injectable } from '@angular/core';
import {globalBackEndUrl} from "../../../environment";
import {map, Observable} from "rxjs";
import {ShowVeicoloResponse} from "../dto/response/ShowVeicoloResponse";
import {HttpClient} from "@angular/common/http";

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
}
