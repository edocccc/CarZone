import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {VeicoloService} from "../../services/veicolo.service";
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";
import {MessageResponse} from "../../dto/response/MessageResponse";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-modifica-veicolo',
  templateUrl: './modifica-veicolo.component.html',
  styleUrls: ['./modifica-veicolo.component.css']
})
//classe che permette di modificare un veicolo al manager
export class ModificaVeicoloComponent implements OnInit{
  //dichiarazione del veicolo da modificare
  veicolo!: ShowDettagliVeicoloResponse;
  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private veicoloService: VeicoloService,private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getVeicolo di veicoloService passandogli l'id del veicolo da modificare
  ngOnInit() {
    this.veicoloService.getVeicolo(this.router.url.split('/')[2]).subscribe({
      next: (response) => {
        //se la richiesta va a buon fine, salva il veicolo nella variabile veicolo e lo stampa in console
        this.veicolo = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error);
      }
    })
  }

  //metodo che permette di modificare un veicolo
  //richiama il metodo modificaVeicolo di veicoloService passandogli i dati del veicolo da modificare
  modificaVeicolo(){
    this.veicoloService.modificaVeicolo(this.veicolo).subscribe({
      next: (response: MessageResponse) => {
        //se la richiesta va a buon fine, stampa la risposta in console e reindirizza alla pagina di gestione veicoli
        console.log(response);
        this.router.navigate(['gestioneVeicoli'])
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error);
      }
    })

  }

  //metodo che permette di reindirizzare alla pagina di gestione veicoli
  redirectGestioneVeicoli() {
    this.router.navigate(['gestioneVeicoli']);
  }
}
