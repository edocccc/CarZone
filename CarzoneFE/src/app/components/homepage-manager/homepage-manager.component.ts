import {Component, OnInit} from '@angular/core';
import {ShowValutazioniDipendenti} from "../../dto/response/ShowValutazioniDipendenti";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {UtenteService} from "../../services/utente.service";

@Component({
  selector: 'app-homepage-manager',
  templateUrl: './homepage-manager.component.html',
  styleUrls: ['./homepage-manager.component.css']
})
//classe che permette di visualizzare la homepage del manager e le informazioni che contiene
export class HomepageManagerComponent implements OnInit{
  //dichiarazione della lista di dipendenti con recensioni da visualizzare
  dipendentiConRecensioni: ShowValutazioniDipendenti[] = [];
  //dichiarazione della variabile accessoEffettuato per verificare se l'utente è loggato
  accessoEffettuato: boolean = !!localStorage.getItem('token');

  //costruttore che inizializza i service necessari e il router per la navigazione
  constructor(private utenteService: UtenteService,private appuntamentoService: AppuntamentoService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getDipendentiConRecensioni di questa classe
  ngOnInit(): void {
    this.getDipendentiConRecensioni();
  }

  //metodo che permette di ottenere i dipendenti con recensioni
  //richiama il metodo getDipendentiConRecensioni del servizio appuntamentoService
  getDipendentiConRecensioni() {
    this.appuntamentoService.getDipendentiConRecensioni().subscribe({
      next: (response: ShowValutazioniDipendenti[]) => {
        //se la richiesta va a buon fine, salva i dipendenti con recensioni nella variabile dipendentiConRecensioni e li stampa in console
        this.dipendentiConRecensioni = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error);
      }}
    );
  }

  //metodo che permette di reindirizzare alla pagina di gestione veicoli
  redirectGestioneVeicoli() {
    this.router.navigate(['gestioneVeicoli']);
  }

  //metodo che permette di reindirizzare alla pagina di gestione utenti
  redirectGestioneUtenti() {
    this.router.navigate(['gestioneUtenti']);
  }

  //metodo che permette di reindirizzare alla pagina di gestione appuntamenti
  redirectGestioneAppuntamenti() {
    this.router.navigate(['gestioneAppuntamenti']);
  }

  //metodo che permette di effettuare il logout
  //richiama il metodo logout del servizio utenteService e imposta la variabile accessoEffettuato a false
  logout(): void {
    this.utenteService.logout();
    this.accessoEffettuato = false;
    this.router.navigate(['homeCliente']);
  }
}
