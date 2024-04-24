import { Component } from '@angular/core';
import {AppuntamentoService} from "../../services/appuntamento.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-prenotazione-veicolo',
  templateUrl: './prenotazione-veicolo.component.html',
  styleUrls: ['./prenotazione-veicolo.component.css']
})
export class PrenotazioneVeicoloComponent {
  dataPrenotazione: Date = new Date();
  idVeicolo: string = this.router.url.split('/')[2];
  idCliente: string = '';

  constructor(private appuntamentoService: AppuntamentoService, private router: Router) { }

  prenota() {
    if(localStorage.getItem('id') != null) {
      // @ts-ignore
      this.idCliente = +localStorage.getItem('id');
    }
    this.appuntamentoService.prenota(this.dataPrenotazione, +this.idVeicolo, +this.idCliente).subscribe({
      next: () => {
        alert('Prenotazione effettuata con successo');
      },
      error: (error: HttpErrorResponse) => {
        alert('Errore durante la prenotazione');
      }
    })
  }
}
