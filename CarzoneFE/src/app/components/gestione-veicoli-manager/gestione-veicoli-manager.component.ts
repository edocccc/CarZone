import {Component, OnInit} from '@angular/core';
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";
import {VeicoloService} from "../../services/veicolo.service";
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-gestione-veicoli-manager',
  templateUrl: './gestione-veicoli-manager.component.html',
  styleUrls: ['./gestione-veicoli-manager.component.css']
})
//classe che permette di visualizzare e gestire i veicoli al manager
export class GestioneVeicoliManagerComponent implements OnInit{
  //dichiarazione della lista di veicoli da visualizzare
  veicoli: ShowDettagliVeicoloManagerResponse[] = [];

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private veicoloService: VeicoloService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getAllVeicoli di questa classe
  ngOnInit(): void {
    this.getAllVeicoli();
  }

  //metodo che permette di ottenere tutti i veicoli per il manager
  //richiama il metodo getAllVeicoli del servizio veicoloService
  private getAllVeicoli() {
    this.veicoloService.getAllVeicoliConDettagli().subscribe({
      next: (response: ShowDettagliVeicoloManagerResponse[] )  => {
        //se la richiesta va a buon fine, salva i veicoli nella variabile veicoli e li stampa in console
        this.veicoli = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante il recupero dei veicoli' + error.message);
      }
    });
  }

  //metodo che permette di eliminare un veicolo
  //richiama il metodo eliminaVeicolo del servizio veicoloService passandogli l'id del veicolo da eliminare
  eliminaVeicolo(id: number) {
    this.veicoloService.eliminaVeicolo(id).subscribe({
      next: () => {
        //se l'eliminazione va a buon fine, richiama il metodo per ottenere tutti i veicoli
        this.getAllVeicoli();
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante l\'eliminazione del veicolo' + error.message);
      }
    });
  }

  //metodo che permette di reindirizzare alla pagina di modifica del veicolo
  redirectModificaVeicolo(id: number) {
    this.router.navigate(['modificaVeicolo/'+id]);
  }

  //metodo che permette di reindirizzare alla pagina di aggiunta di un veicolo
  redirectAggiungiVeicolo() {
    this.router.navigate(['aggiungiVeicolo']);
  }

  //metodo che permette di reindirizzare alla homepage del manager
  redirectHomepageManager() {
    this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
  }
}
