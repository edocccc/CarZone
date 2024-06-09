import {Component, OnInit} from '@angular/core';
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {MessageResponse} from "../../dto/response/MessageResponse";
import {HttpErrorResponse} from "@angular/common/http";
import {ShowUtenteManagerResponse} from "../../dto/response/ShowUtenteManagerResponse";
import {UtenteService} from "../../services/utente.service";

@Component({
  selector: 'app-modifica-utente',
  templateUrl: './modifica-utente.component.html',
  styleUrls: ['./modifica-utente.component.css']
})
//classe che permette di modificare un utente al manager
export class ModificaUtenteComponent implements OnInit{
  //dichiarazione dell'utente da modificare
  utente!: ShowUtenteManagerResponse;
  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private utenteService: UtenteService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getUtente di utenteService passandogli l'id dell'utente da modificare
  ngOnInit() {
    this.utenteService.getUtente(+this.router.url.split('/')[2]).subscribe({
      next: (response) => {
        //se la richiesta va a buon fine, salva l'utente nella variabile utente e lo stampa in console
        this.utente = response;
        console.log(response);
      },
      error: (error) => {
        //se si verifica un errore, lo stampa in console
        console.log(error);
      }
    })
  }

  //metodo che permette di modificare un utente
  //richiama il metodo modificaUtente di utenteService passandogli i dati dell'utente da modificare
  modificaUtente(){
    this.utenteService.modificaUtente(this.utente).subscribe({
      next: (response: MessageResponse) => {
        //se la richiesta va a buon fine, stampa la risposta in console e reindirizza alla pagina di gestione utenti
        console.log(response);
        this.router.navigate(['gestioneUtenti'])
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error);
      }
    })

  }

  //metodo che permette di reindirizzare alla pagina di gestione utenti
  redirectGestioneUtenti() {
    this.router.navigate(['gestioneUtenti']);
  }
}
