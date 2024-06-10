import {Component, OnInit} from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {ShowVeicoloResponse} from "../../dto/response/ShowVeicoloResponse";
import {UtenteService} from "../../services/utente.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-homepage-cliente',
  templateUrl: './homepage-cliente.component.html',
  styleUrls: ['./homepage-cliente.component.css']
})
//classe che permette di visualizzare la homepage del cliente e le informazioni che contiene
export class HomepageClienteComponent implements OnInit{
  //dichiarazione della lista di veicoli da visualizzare
  veicoli: ShowVeicoloResponse[] = [];
  //dichiarazione della variabile accessoEffettuato per verificare se l'utente è loggato
  accessoEffettuato: boolean = !!localStorage.getItem('token');
  //dichiarazione della variabile targaVeicolo per la ricerca di un veicolo
  statoVeicolo: string = '';

  //costruttore che inizializza i service necessari e il router per la navigazione
  constructor(private veicoloService: VeicoloService, private utenteService: UtenteService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getVeicoli di questa classe e verifica se l'utente è loggato con il metodo accessoEffettuato del service utenteService
  ngOnInit(): void {
    this.getVeicoli();
    this.accessoEffettuato = this.utenteService.accessoEffettuato();
  }

  //metodo che permette di ottenere tutti i veicoli
  //richiama il metodo getVeicoli del servizio veicoloService
  getVeicoli(): void {
    this.veicoloService.getVeicoli().subscribe({
      next: (response) => {
        //se la richiesta va a buon fine, salva i veicoli nella variabile veicoli e li stampa in console
        this.veicoli = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console e imposta la variabile veicoli ad array vuoto
        console.log("Si è verificato un errore:" + error.message);
        this.veicoli = [];
      },
    });
  }

  //metodo che permette di effettuare il logout
  //richiama il metodo logout del servizio utenteService e imposta la variabile accessoEffettuato a false
  logout(): void {
    this.utenteService.logout();
    this.accessoEffettuato = false;
  }

  //metodo che permette di reindirizzare alla pagina che contiene gli appuntamenti del cliente
  redirectIMieiAppuntamenti() {
    this.router.navigate(['mieiAppuntamenti/'+ localStorage.getItem('id')]);
  }

  //metodo che permette di reindirizzare alla pagina accesso
  redirectLogin() {
    this.router.navigate(['login/']);
  }

  //metodo che permette di reindirizzare alla pagina di ricerca dei veicoli
  redirectRicerca() {
    this.router.navigate(['cerca/']);
  }

  //metodo che permette di reindirizzare alla pagina di visualizzazione dei dettagli di un veicolo
  redirectDettagliVeicolo(id: number) {
    this.router.navigate(['dettagli/'+ id.toString()]);
  }
}
