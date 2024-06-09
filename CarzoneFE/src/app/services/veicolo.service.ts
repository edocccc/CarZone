import { Injectable } from '@angular/core';
import {globalBackEndUrl} from "../../../environment";
import {map, Observable} from "rxjs";
import {ShowVeicoloResponse} from "../dto/response/ShowVeicoloResponse";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ShowDettagliVeicoloResponse} from "../dto/response/ShowDettagliVeicoloResponse";
import {RicercaRequest} from "../dto/request/RicercaRequest";
import {RegistrazioneVenditaRequest} from "../dto/request/RegistrazioneVenditaRequest";
import {MessageResponse} from "../dto/response/MessageResponse";
import {ShowDettagliVeicoloManagerResponse} from "../dto/response/ShowDettagliVeicoloManagerResponse";

@Injectable({
  providedIn: 'root'
})
//classe che si occupa di gestire le chiamate al back-end per la gestione dei veicoli
export class VeicoloService {
  //definizione dell'url principale del back-end per la gestione dei veicoli
  private backEndUrl: string = globalBackEndUrl + 'veicolo/';

  //costruttore con il client http
  constructor(private http: HttpClient) { }

  //metodo che si occupa di ottenere tutti i veicoli
  getVeicoli(): Observable<ShowVeicoloResponse[]> {
    //chiamata al back-end per ottenere tutti i veicoli
    return this.http.get<ShowVeicoloResponse[]>(this.backEndUrl + 'veicoli').pipe(
      map(response => {
        return response;
      })
    );
  }

  //metodo che si occupa di ottenere un veicolo tramite l'id passato come parametro
  getVeicolo(id: string): Observable<ShowDettagliVeicoloManagerResponse> {
    //controllo che l'id non sia nullo
    if(id == null || id == "" ){
        throw new Error("id non può essere nullo");
    }
    //chiamata al back-end per ottenere il veicolo tramite l'id
    return this.http.get<ShowDettagliVeicoloManagerResponse>(this.backEndUrl + 'dettagli/' + id);
  }

  //metodo che si occupa di cercare un veicolo tramite i parametri passati come parametro
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
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
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
    //chiamata al back-end per la ricerca del veicolo
    return this.http.post<ShowVeicoloResponse[]>(this.backEndUrl + 'cerca', request, {headers: token});
  }

  //metodo che si occupa di registrare una vendita tramite i parametri passati come parametro
  registraVendita(idAppuntamento: number, venditaConclusa: boolean): Observable<MessageResponse>{
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //creazione della request tramite il DTO
    const request: RegistrazioneVenditaRequest = {
        idAppuntamento,
        venditaConclusa
    }
    //chiamata al back-end per registrare la vendita
    return this.http.post<MessageResponse>(this.backEndUrl + 'registraVendita', request, {headers: token});
  }

  //metodo che si occupa di ottenere tutti i veicoli con i dettagli
  getAllVeicoliConDettagli(): Observable<ShowDettagliVeicoloManagerResponse[]> {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per ottenere tutti i veicoli con i dettagli
    return this.http.get<ShowDettagliVeicoloManagerResponse[]>(this.backEndUrl + 'veicoliConDettagli', {headers: token});
  }

  //metodo che si occupa di eliminare un veicolo tramite l'id passato come parametro
  eliminaVeicolo(id: number): Observable<MessageResponse> {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per eliminare il veicolo
    return this.http.delete<MessageResponse>(this.backEndUrl + 'elimina/' + id, {headers: token});
  }

  //metodo che si occupa di modificare un veicolo tramite i dati passati come parametro
  modificaVeicolo(veicolo: ShowDettagliVeicoloResponse) {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per modificare il veicolo
    return this.http.put<MessageResponse>(this.backEndUrl + 'modifica/' + veicolo.id.toString(), veicolo, {headers: token});
  }

  //metodo che si occupa di aggiungere un veicolo tramite i dati passati come parametro
  aggiungiVeicolo(
    targa: string,
    marca: string,
    modello: string,
    chilometraggio: number,
    annoProduzione: number,
    potenzaCv: number,
    alimentazione: string,
    prezzo: number,
    fileSelezionato: any) : Observable<MessageResponse> {
    const token: HttpHeaders = this.recuperaToken();
    //aggiunta dei dati come parametri della richiesta
    const formData = new FormData();
    formData.append("targa", targa);
    formData.append("marca", marca);
    formData.append("modello", modello);
    formData.append("chilometraggio", chilometraggio.toString());
    formData.append("annoProduzione", annoProduzione.toString());
    formData.append("potenzaCv", potenzaCv.toString());
    formData.append("alimentazione", alimentazione);
    formData.append("prezzo", prezzo.toString());
    formData.append("immagine", fileSelezionato);

    //chiamata al back-end per aggiungere il veicolo
    return this.http.post<MessageResponse>(this.backEndUrl + 'aggiungiVeicolo', formData, {headers: token});
  }

  //metodo che si occupa di ottenere tutti i veicoli disponibili
  getVeicoliDisponibili() {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per ottenere tutti i veicoli disponibili
    return this.http.get<ShowDettagliVeicoloManagerResponse[]>(this.backEndUrl + 'veicoliDisponibili', {headers: token}).pipe(
      map(response => {
        return response;
      })
    );
  }

  //metodo che recupera il token per l'autenticazione
  private recuperaToken(): HttpHeaders {
    //recupero del token dal local storage
    const token = localStorage.getItem('token');
    //ritorno il token come header
    return new HttpHeaders({'Authorization': 'Bearer ' + token})
  }

  //metodo che si occupa di ottenere tutti i veicoli disponibili e quello selezionato
  getVeicoliDisponibiliESelezionato(idAppuntamento: number) {
    //recupero del token per l'autenticazione
    const token: HttpHeaders = this.recuperaToken();
    //chiamata al back-end per ottenere tutti i veicoli disponibili e quello selezionato
    return this.http.get<ShowDettagliVeicoloManagerResponse[]>(this.backEndUrl + 'veicoliDisponibiliESelezionato/' + idAppuntamento, {headers: token}).pipe(
      map(response => {
        return response;
      })
    );
  }
}
