import {Component, OnInit} from '@angular/core';
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {ShowUtenteManagerResponse} from "../../dto/response/ShowUtenteManagerResponse";
import {UtenteService} from "../../services/utente.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-gestione-utenti-manager',
  templateUrl: './gestione-utenti-manager.component.html',
  styleUrls: ['./gestione-utenti-manager.component.css']
})
//classe che permette di visualizzare e gestire gli utenti del manager
export class GestioneUtentiManagerComponent implements OnInit{
  //dichiarazione della lista di utenti da visualizzare
  utenti: ShowUtenteManagerResponse[] = [];

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private utenteService: UtenteService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getAllUtentiManager di questa classe
  ngOnInit(): void {
    this.getAllUtentiManager();
  }

  //metodo che permette di ottenere tutti gli utenti per il manager
  //richiama il metodo getAllUtentiManager del servizio utenteService
  getAllUtentiManager() {
    this.utenteService.getAllUtentiManager().subscribe({
      next: (response: ShowUtenteManagerResponse[] )  => {
        //se la richiesta va a buon fine, salva gli utenti nella variabile utenti e li stampa in console
        this.utenti = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante il recupero degli utenti' + error.message);
      }
    });
  }

  //metodo che permette di eliminare un utente
  //richiama il metodo eliminaUtente del servizio utenteService passandogli l'id dell'utente da eliminare
  eliminaUtente(id: number) {
    this.utenteService.eliminaUtente(id).subscribe({
      next: () => {
        //se l'eliminazione va a buon fine, richiama il metodo per ottenere tutti gli utenti
        this.getAllUtentiManager();
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante l\'eliminazione dell\'utente' + error.message);
      }
    });
  }

  //metodo che permette di reindirizzare alla pagina di modifica dell'utente
  redirectModificaUtente(id: number) {
    this.router.navigate(['modificaUtente/'+id]);
  }

  //metodo che permette di reindirizzare alla pagina di aggiunta di un dipendente
  redirectAggiungiDipendente() {
    this.router.navigate(['registraDipendente']);
  }

  //metodo che permette di reindirizzare alla homepage del manager
  redirectHomepageManager() {
    this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
  }
}
