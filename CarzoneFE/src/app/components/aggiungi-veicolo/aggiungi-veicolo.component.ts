import { Component } from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";

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

  constructor(private veicoloService: VeicoloService) { }

  aggiungiVeicolo() {
    this.veicoloService.aggiungiVeicolo(
      this.targa,
      this.marca,
      this.modello,
      this.chilometraggio,
      this.annoProduzione,
      this.potenzaCv,
      this.alimentazione,
      this.prezzo).subscribe({
      next: () => {
        console.log('Veicolo aggiunto con successo');
      },
      error: () => {
        console.log('Errore durante l\'aggiunta del veicolo');
      }
    });

  }
}
