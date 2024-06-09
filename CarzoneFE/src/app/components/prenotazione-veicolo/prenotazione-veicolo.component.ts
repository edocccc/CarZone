import { Component } from '@angular/core';
import {AppuntamentoService} from "../../services/appuntamento.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-prenotazione-veicolo',
  templateUrl: './prenotazione-veicolo.component.html',
  styleUrls: ['./prenotazione-veicolo.component.css']
})
//classe che permette di prenotare un veicolo
export class PrenotazioneVeicoloComponent {
  //dichiarazione della data di prenotazione, dell'id del veicolo e dell'id del cliente
  dataPrenotazione: Date = new Date();
  idVeicolo: string = this.router.url.split('/')[2];
  idCliente: string = '';

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private appuntamentoService: AppuntamentoService, private router: Router) { }

  //metodo che permette di prenotare un veicolo
  //richiama il metodo prenota del servizio appuntamentoService passandogli i parametri necessari per la prenotazione
  prenota() {
    if(localStorage.getItem('id') != null) {
      // @ts-ignore
      this.idCliente = +localStorage.getItem('id');
    }
    this.appuntamentoService.prenota(this.dataPrenotazione, +this.idVeicolo, +this.idCliente).subscribe({
      next: () => {
        //se la prenotazione va a buon fine, reindirizza alla home del cliente
        this.router.navigate(['homeCliente/']);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante la prenotazione' + error.error.message);
      }
    })
  }

  //metodo che permette di reindirizzare alla home del cliente
  redirectHomepageCliente() {
    this.router.navigate(['homeCliente/']);
  }
}
