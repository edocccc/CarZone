import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {HttpErrorResponse} from "@angular/common/http";
import {MessageResponse} from "../../dto/response/MessageResponse";

@Component({
  selector: 'app-lascia-recensione',
  templateUrl: './lascia-recensione.component.html',
  styleUrls: ['./lascia-recensione.component.css']
})
//classe che permette di lasciare una recensione al dipendente
export class LasciaRecensioneComponent {
  //dichiarazione delle variabili da utilizzare per la recensione
  idAppuntamento: number = +this.router.url.split('/')[2];
  votoRecensione: number = 0;
  testoRecensione: string = '';

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private appuntamentoService: AppuntamentoService, private router: Router) { }

  //metodo che permette di inviare la recensione
  //richiama il metodo inviaRecensione del servizio appuntamentoService passandogli i parametri necessari per l'invio
  inviaRecensione() {
    this.appuntamentoService.inviaRecensione(this.idAppuntamento, this.votoRecensione, this.testoRecensione).subscribe({
      next: (response: MessageResponse) => {
        //se l'invio va a buon fine, reindirizza alla pagina dei miei appuntamenti
        this.router.navigate(['mieiAppuntamenti/'+ localStorage.getItem('id')]);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error.error);
      }
    })
  }

  //metodo che permette di reindirizzare alla pagina dei miei appuntamenti
  redirectMieiAppuntamenti() {
    this.router.navigate(['mieiAppuntamenti/' + localStorage.getItem('id')]);
  }
}
