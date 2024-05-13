import { Component } from '@angular/core';
import {UtenteService} from "../../services/utente.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-aggiungi-dipendente',
  templateUrl: './aggiungi-dipendente.component.html',
  styleUrls: ['./aggiungi-dipendente.component.css']
})
export class AggiungiDipendenteComponent {
  email: string = '';
  nome: string = '';
  cognome: string = '';
  dataNascita: Date = new Date();
  username: string = '';
  password: string = '';
  passwordRipetuta: string = '';

  constructor(private utenteService: UtenteService, private router: Router) {  }

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
          console.log(response);
        },
        error: (error) => {
          console.log(error.error.message);
        },
      });
  }

  redirectGestioneUtenti() {
    this.router.navigate(['gestioneUtenti' ]);
  }
}
