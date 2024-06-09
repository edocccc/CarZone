import {Component, OnInit} from '@angular/core';
import {ShowAppuntamentoManagerResponse} from "../../dto/response/ShowAppuntamentoManagerResponse";
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";
import {ShowUtenteManagerResponse} from "../../dto/response/ShowUtenteManagerResponse";
import {UtenteService} from "../../services/utente.service";
import {VeicoloService} from "../../services/veicolo.service";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {MessageResponse} from "../../dto/response/MessageResponse";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {ShowAppuntamentoModificaResponse} from "../../dto/response/ShowAppuntamentoModificaResponse";

@Component({
  selector: 'app-modifica-appuntamento',
  templateUrl: './modifica-appuntamento.component.html',
  styleUrls: ['./modifica-appuntamento.component.css']
})
//classe che permette di modificare un appuntamento al manager
export class ModificaAppuntamentoComponent implements OnInit{
  //dichiarazione dell'appuntamento da modificare
  appuntamento!: ShowAppuntamentoModificaResponse;
  //dichiarazione dell'id dell'appuntamento da modificare
  idAppuntamento: number = +this.router.url.split('/')[2];

  //dichiarazione delle liste di veicoli, clienti e dipendenti
  veicoli: ShowDettagliVeicoloManagerResponse[] = [];
  clienti: ShowUtenteManagerResponse[] = [];
  dipendenti: ShowUtenteManagerResponse[] = [];

  //costruttore che inizializza i service necessari e il router per la navigazione
  constructor(private utenteService: UtenteService, private veicoloService: VeicoloService, private appuntamentoService: AppuntamentoService , private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama i metodi per ottenere l'appuntamento da modificare, i clienti, i dipendenti e i veicoli da questa classe
  ngOnInit(): void {
    this.getAppuntamento(this.idAppuntamento);
    this.getClienti();
    this.getDipendenti();
    this.getVeicoliDisponibiliESelezionato(this.idAppuntamento);
  }

  //metodo che permette di ottenere l'appuntamento da modificare
  //richiama il metodo getAppuntamento del servizio appuntamentoService passandogli l'id dell'appuntamento
  getAppuntamento(idAppuntamento: number) {
    this.appuntamentoService.getAppuntamento(idAppuntamento).subscribe({
      next: (response: ShowAppuntamentoModificaResponse) => {
        //se la richiesta va a buon fine, salva l'appuntamento nella variabile appuntamento e lo stampa in console
        this.appuntamento = response;
        console.log("appuntamento trovato: "+response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.error);
      },
    });
  }

  //metodo che permette di modificare un appuntamento
  //richiama il metodo modificaAppuntamento del servizio appuntamentoService passandogli l'id dell'appuntamento da modificare e i dati dell'appuntamento modificato
  modificaAppuntamento() {
    this.appuntamentoService.modificaAppuntamento(this.idAppuntamento, this.appuntamento.dataOra, this.appuntamento.idVeicolo, this.appuntamento.idCliente, this.appuntamento.idDipendente).subscribe({
      next: (response:MessageResponse) => {
        //se la richiesta va a buon fine, reindirizza alla pagina di gestione appuntamenti e stampa la risposta in console
        this.router.navigate(['gestioneAppuntamenti']);
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.error);
      },
    });
  }

  //metodo che permette di ottenere i veicoli disponibili e il veicolo selezionato
  //richiama il metodo getVeicoliDisponibiliESelezionato del servizio veicoloService passandogli l'id dell'appuntamento
  getVeicoliDisponibiliESelezionato(idAppuntamento: number) {
    this.veicoloService.getVeicoliDisponibiliESelezionato(idAppuntamento).subscribe({
      next: (response: ShowDettagliVeicoloManagerResponse[]) => {
        //se la richiesta va a buon fine, salva i veicoli nella variabile veicoli e li stampa in console
        this.veicoli = response;
        console.log("veicoli disponibili e selzionato:" + response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.error);
      },
    });
  }

  //metodo che permette di ottenere tutti i clienti
  //richiama il metodo getClienti del servizio utenteService
  getClienti() {
    this.utenteService.getClienti().subscribe({
      next: (response: ShowUtenteManagerResponse[]) => {
        //se la richiesta va a buon fine, salva i clienti nella variabile clienti e li stampa in console
        this.clienti = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.message);
      },
    });
  }

  //metodo che permette di ottenere tutti i dipendenti
  //richiama il metodo getDipendenti del servizio utenteService
  private getDipendenti() {
    this.utenteService.getDipendenti().subscribe({
      next: (response: ShowUtenteManagerResponse[]) => {
        //se la richiesta va a buon fine, salva i dipendenti nella variabile dipendenti e li stampa in console
        this.dipendenti = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.message);
      },
    });
  }

  //metodo che permette di reindirizzare alla pagina di gestione appuntamenti
  redirectGestioneAppuntamenti() {
    this.router.navigate(['gestioneAppuntamenti' ]);
  }
}
