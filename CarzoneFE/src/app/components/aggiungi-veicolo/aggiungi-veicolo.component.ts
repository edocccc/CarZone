import { Component } from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-aggiungi-veicolo',
  templateUrl: './aggiungi-veicolo.component.html',
  styleUrls: ['./aggiungi-veicolo.component.css']
})
export class AggiungiVeicoloComponent {
  targa: string = '';
  marca: string = '';
  modello: string = '';
  chilometraggio: number = 0;
  annoProduzione: number = 0;
  potenzaCv: number = 0;
  alimentazione: string = '';
  prezzo: number = 0;
  fileSelezionato = null;

  constructor(private veicoloService: VeicoloService, private router:Router) { }

  aggiungiVeicolo() {
    this.veicoloService.aggiungiVeicolo(
      this.targa,
      this.marca,
      this.modello,
      this.chilometraggio,
      this.annoProduzione,
      this.potenzaCv,
      this.alimentazione,
      this.prezzo,
      this.fileSelezionato
    ).subscribe({
      next: () => {
        console.log('Veicolo aggiunto con successo');
      },
      error: (error: HttpErrorResponse) => {
        console.log('Errore durante l\'aggiunta del veicolo' + error.message);
      }
    });

  }

  onFileSelected(event: any) {
    this.fileSelezionato = event.target.files[0];
  }

  redirectHomepageManager() {
    this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
  }
}
