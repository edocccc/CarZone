import { Component } from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-aggiungi-veicolo',
  templateUrl: './aggiungi-veicolo.component.html',
  styleUrls: ['./aggiungi-veicolo.component.css']
})
//classe che permette di aggiungere un veicolo al manager
export class AggiungiVeicoloComponent {
  //dichiarazione delle variabili da utilizzare per l'aggiunta di un veicolo
  targa: string = '';
  marca: string = '';
  modello: string = '';
  chilometraggio: number = 0;
  annoProduzione: number = 0;
  potenzaCv: number = 0;
  alimentazione: string = '';
  prezzo: number = 0;
  fileSelezionato = null;

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private veicoloService: VeicoloService, private router:Router) { }

  //metodo che permette di aggiungere un veicolo
  //richiama il metodo aggiungiVeicolo del servizio veicoloService passandogli i parametri necessari per l'aggiunta
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
        //se l'aggiunta va a buon fine, reindirizza alla homepage del manager
        //la risposta viene stampata in console
        console.log('Veicolo aggiunto con successo');
        this.redirectHomepageManager();
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante l\'aggiunta del veicolo' + error.message);
      }
    });

  }

  //metodo che permette di selezionare un file da aggiungere come immagine del veicolo
  onFileSelected(event: any) {
    this.fileSelezionato = event.target.files[0];
  }

  //metodo che permette di reindirizzare alla homepage del manager dopo aver aggiunto un veicolo
  redirectHomepageManager() {
    this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
  }
}
