import { Component } from '@angular/core';
import {UtenteService} from "../../services/utente.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-aggiungi-dipendente',
  templateUrl: './aggiungi-dipendente.component.html',
  styleUrls: ['./aggiungi-dipendente.component.css']
})
//classe che permette di aggiungere un dipendente al manager
export class AggiungiDipendenteComponent {
  //dichiarazione delle variabili da utilizzare per l'aggiunta di un dipendente
  email: string = '';
  nome: string = '';
  cognome: string = '';
  dataNascita: Date = new Date();
  username: string = '';
  password: string = '';
  passwordRipetuta: string = '';

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private utenteService: UtenteService, private router: Router) {  }

  //metodo che permette di registrare un dipendente
  //richiama il metodo registraDipendente del servizio utenteService passandogli i parametri necessari per la registrazione
  registraDipendente() {
    this.utenteService
      .registraDipendente(
        this.email,
        this.nome,
        this.cognome,
        this.dataNascita,
        this.username,
        this.password,
        this.passwordRipetuta
      )
      .subscribe({
        next: (response) => {
          //se la registrazione va a buon fine, reindirizza alla pagina di gestione utenti
          //la risposta viene stampata in console
          console.log(response);
          this.redirectGestioneUtenti();
        },
        error: (error: HttpErrorResponse) => {
          //se si verifica un errore, lo stampa in console
          console.log(error.error.message);
        },
      });
  }

  //metodo che permette di reindirizzare alla pagina di gestione utenti dopo aver registrato un dipendente
  redirectGestioneUtenti() {
    this.router.navigate(['gestioneUtenti' ]);
  }
}
