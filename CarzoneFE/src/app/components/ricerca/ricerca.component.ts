import { Component } from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {ShowVeicoloResponse} from "../../dto/response/ShowVeicoloResponse";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-ricerca',
  templateUrl: './ricerca.component.html',
  styleUrls: ['./ricerca.component.css']
})
//classe che permette di ricercare un veicolo
export class RicercaComponent {
  //dichiarazione della lista di veicoli da visualizzare e della variabile ricercaEffettuata
  veicoli: ShowVeicoloResponse[] = [];
  ricercaEffettuata: boolean = false;

  //dichiarazione delle variabili per la ricerca
  criterio: string = "";
  targa: string = "";
  marca: string = "";
  modello: string = "";
  potenzaMinima: number = 0;
  potenzaMassima: number = 0;
  prezzoMinimo: number = 0;
  prezzoMassimo: number = 0;
  alimentazione: string = "";
  annoProduzioneMinimo: number = 0;
  annoProduzioneMassimo: number = 0;
  chilometraggioMinimo: number = 0;
  chilometraggioMassimo: number = 0;

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private veicoloService: VeicoloService, private router: Router) { }

  //metodo che permette di ricercare un veicolo
  //richiama il metodo ricerca del servizio veicoloService passandogli i dati della ricerca
  ricerca() {
    this.veicoloService.ricerca(
      this.criterio,
      this.targa,
      this.marca,
      this.modello,
      this.potenzaMinima,
      this.potenzaMassima,
      this.prezzoMinimo,
      this.prezzoMassimo,
      this.alimentazione,
      this.annoProduzioneMinimo,
      this.annoProduzioneMassimo,
      this.chilometraggioMinimo,
      this.chilometraggioMassimo
    ).subscribe({
      next: (response) => {
        //se la ricerca va a buon fine, salva i veicoli nella variabile veicoli e la variabile ricercaEffettuata a true
        this.veicoli = response;
        this.ricercaEffettuata = true;
        //stampa i veicoli in console
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console e imposta la variabile veicoli ad array vuoto
        console.log(error.error.message);
        this.veicoli = [];
      },
    });
  }

  //metodo che permette di reindirizzare alla homepage del cliente
  redirectHomepageCliente() {
    this.router.navigate(['homeCliente/']);
  }
}
