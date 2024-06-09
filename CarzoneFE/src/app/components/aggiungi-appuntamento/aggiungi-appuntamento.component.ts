import {Component, OnInit} from '@angular/core';
import {AppuntamentoService} from "../../services/appuntamento.service";
import {ShowUtenteManagerResponse} from "../../dto/response/ShowUtenteManagerResponse";
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";
import {UtenteService} from "../../services/utente.service";
import {VeicoloService} from "../../services/veicolo.service";
import {HttpErrorResponse} from "@angular/common/http";
import {MessageResponse} from "../../dto/response/MessageResponse";
import {Router} from "@angular/router";

@Component({
  selector: 'app-aggiungi-appuntamento',
  templateUrl: './aggiungi-appuntamento.component.html',
  styleUrls: ['./aggiungi-appuntamento.component.css']
})
//classe che permette di aggiungere un appuntamento al manager
export class AggiungiAppuntamentoComponent implements OnInit{
  //dichiarazione delle variabili da utilizzare per l'aggiunta di un appuntamento
  dataOra: Date = new Date();
  idVeicolo: number = 0;
  idCliente: number = 0;
  idDipendente: number = 0;

  //dichiarazione delle liste di veicoli, clienti e dipendenti
  veicoli: ShowDettagliVeicoloManagerResponse[] = [];
  clienti: ShowUtenteManagerResponse[] = [];
  dipendenti: ShowUtenteManagerResponse[] = [];
  //costruttore che inizializza i service necessari e il router per la navigazione
  constructor(private utenteService: UtenteService, private veicoloService: VeicoloService, private appuntamentoService: AppuntamentoService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama i metodi per ottenere i veicoli disponibili, i clienti e i dipendenti
  ngOnInit(): void {
    this.getVeicoliDisponibili();
    this.getClienti();
    this.getDipendenti();
  }

  //metodo che permette di prenotare un appuntamento
  //richiama il metodo prenotaAppuntamento del servizio appuntamentoService passandogli i parametri necessari per la prenotazione
  prenota() {
    this.appuntamentoService.prenotaAppuntamento(this.dataOra, this.idVeicolo, this.idCliente, this.idDipendente).subscribe({
      next: (response:MessageResponse) => {
        //se la prenotazione va a buon fine, reindirizza alla pagina di gestione appuntamenti
        console.log(response);
        this.router.navigate(['gestioneAppuntamenti' ]);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error);
      },
    });
  }

  //metodo che permette di ottenere i veicoli disponibili
  //richiama il metodo getVeicoliDisponibili del servizio veicoloService
  getVeicoliDisponibili() {
    this.veicoloService.getVeicoliDisponibili().subscribe({
      next: (response: ShowDettagliVeicoloManagerResponse[]) => {
        //se la richiesta va a buon fine, salva i veicoli nella variabile veicoli e li stampa in console
        this.veicoli = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error);
      },
    });
  }

  //metodo che permette di ottenere i clienti
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

  //metodo che permette di ottenere i dipendenti
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

  //metodo che permette di reindirizzare alla pagina di gestione appuntamenti dopo aver aggiunto un appuntamento
  redirectGestioneAppuntamenti() {
    this.router.navigate(['gestioneAppuntamenti' ]);
  }
}
