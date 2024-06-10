import { Component } from '@angular/core';
import { UtenteService } from 'src/app/services/utente.service';
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-registrazione',
  templateUrl: './registrazione.component.html',
  styleUrls: ['./registrazione.component.css'],
})
//classe che permette di registrare un utente
export class RegistrazioneComponent {
  //dichiarazione delle variabili email, nome, cognome, data di nascita, username, password e password ripetuta
  protected email: string = '';
  protected nome: string = '';
  protected cognome: string = '';
  protected dataNascita: Date = new Date();
  protected username: string = '';
  protected password: string = '';
  protected passwordRipetuta: string = '';

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private utenteService: UtenteService, private router: Router) {}

  //metodo che permette di registrare un utente
  //richiama il metodo registra del servizio utenteService passandogli i dati dell'utente da registrare
  registra(): void {
    this.utenteService
      .registra(
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
          //se la registrazione va a buon fine, stampa la risposta in console
          console.log(response);
          //reindirizza alla pagina di login
          this.redirectLogin();
        },
        error: (error: HttpErrorResponse) => {
          //se si verifica un errore, lo stampa in console
          console.log(error.error.message);
        },
      });
  }

  //metodo che permette di reindirizzare alla pagina di login
  redirectLogin() {
    this.router.navigate(['login/']);
  }
}
