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
          this.router.navigate(['/homeCliente']);
        },
        error: (error) => {
          console.log(error.error.message);
        },
      });
  }

}
