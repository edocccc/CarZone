import { Component } from '@angular/core';
import {UtenteService} from "../../services/utente.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  protected username: string = '';
  protected password: string = '';

  constructor(private utenteService: UtenteService, private router: Router) {}

  login(): void {
    this.utenteService
      .login(
        this.username,
        this.password
      )
      .subscribe({
        next: (response) => {
          console.log(response);
          localStorage.setItem('token', response.token);
          localStorage.setItem('username', this.username);
          localStorage.setItem('ruolo', response.ruolo);
          localStorage.setItem('id', response.id.toString());
          localStorage.setItem('email', response.email);
          localStorage.setItem('nome', response.nome);
          localStorage.setItem('cognome', response.cognome);
          localStorage.setItem('dataNascita', response.dataNascita.toString());
          if (response.ruolo === 'MANAGER') this.router.navigate(['/homeManager/'+response.id]);
          else if (response.ruolo === 'DIPENDENTE') this.router.navigate(['/homeDipendente/'+response.id]);
          else this.router.navigate(['/homeCliente']);
        },
        error: (error) => {
          console.log(error.error.message);
        },
      });
  }

}
