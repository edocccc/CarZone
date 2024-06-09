import { Component } from '@angular/core';
import {AppuntamentoService} from "../../services/appuntamento.service";
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-registra-vendita',
  templateUrl: './registra-vendita.component.html',
  styleUrls: ['./registra-vendita.component.css']
})
//classe che permette di registrare una vendita
export class RegistraVenditaComponent {
  //dichiarazione dell'id dell'appuntamento da registrare e della variabile booleana che indica se la vendita è stata conclusa
  idAppuntamento: number = +this.router.url.split('/')[2];
  venditaConclusa: boolean = false;

  //costruttore che inizializza i service necessari e il router per la navigazione
  constructor(private veicoloService: VeicoloService, private router: Router) { }


  //metodo che permette di registrare una vendita
  //richiama il metodo registraVendita del servizio veicoloService passandogli l'id dell'appuntamento da registrare e la variabile booleana che indica se la vendita è stata conclusa
  registraVendita() {
    this.veicoloService.registraVendita(this.idAppuntamento, this.venditaConclusa).subscribe({
      next: () => {
        //se la richiesta va a buon fine, stampa un messaggio di successo in console
        console.log('Esito della vendita registrato con successo');
        //reindirizza alla home del dipendente o del manager in base al ruolo
        if (localStorage.getItem('ruolo') === 'DIPENDENTE') {
          // se l'utente è un dipendente lo reindirizza alla home del dipendente
          this.router.navigate(['homeDipendente/' + localStorage.getItem('id')]);
        } else {
          // se l'utente è un manager lo reindirizza alla home del manager
          this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
        }
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante la registrazione della vendita' + error.error);
      }
    });
  }

  //metodo che permette di reindirizzare alla home del dipendente o del manager in base al ruolo
  redirectHomepage() {
    if (localStorage.getItem('ruolo') === 'DIPENDENTE') {
      this.router.navigate(['homeDipendente/' + localStorage.getItem('id')]);
    } else {
      this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
    }
  }
}
