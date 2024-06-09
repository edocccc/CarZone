import { Component } from '@angular/core';
import {UtenteService} from "../../services/utente.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
//classe che permette di effettuare il login
export class LoginComponent {
  //dichiarazione delle variabili username e password
  protected username: string = '';
  protected password: string = '';

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private utenteService: UtenteService, private router: Router) {}

  //metodo che permette di effettuare il login
  //richiama il metodo login del servizio utenteService passandogli username e password
  login(): void {
    this.utenteService
      .login(
        this.username,
        this.password
      )
      .subscribe({
        next: (response) => {
          //se il login va a buon fine, salva i dati dell'utente nel localStorage
          //stampa in console la risposta
          console.log(response);
          localStorage.setItem('token', response.token);
          localStorage.setItem('username', this.username);
          localStorage.setItem('ruolo', response.ruolo);
          localStorage.setItem('id', response.id.toString());
          localStorage.setItem('email', response.email);
          localStorage.setItem('nome', response.nome);
          localStorage.setItem('cognome', response.cognome);
          localStorage.setItem('dataNascita', response.dataNascita.toString());
          //se il ruolo è MANAGER reindirizza alla homepage del manager
          if (response.ruolo === 'MANAGER') this.router.navigate(['/homeManager/'+response.id]);
          //se il ruolo è DIPENDENTE reindirizza alla homepage del dipendente
          else if (response.ruolo === 'DIPENDENTE') this.router.navigate(['/homeDipendente/'+response.id]);
          //altrimenti reindirizza alla homepage del cliente
          else this.router.navigate(['/homeCliente']);
        },
        error: (error: HttpErrorResponse) => {
          //se si verifica un errore, lo stampa in console
          console.log(error.error.message);
        },
      });
  }

}
