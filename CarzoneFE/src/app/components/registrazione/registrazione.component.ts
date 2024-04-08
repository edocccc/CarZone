import { Component } from '@angular/core';
import { RegisterRequest } from 'src/app/dto/request/RegisterRequest';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-registrazione',
  templateUrl: './registrazione.component.html',
  styleUrls: ['./registrazione.component.css'],
})
export class RegistrazioneComponent {
  protected email: string = '';
  protected dataNascita: Date = new Date();
  protected username: string = '';
  protected password: string = '';
  protected passwordRipetuta: string = '';

  constructor(private authService: AuthService) {}

  registra(): void {
    this.authService
      .registra(this.email, this.dataNascita, this.username, this.password)
      .subscribe({
        next: (response) => {
          console.log(response);
        },
        error: (error) => {
          console.log(error.error.message);
        },
      });
  }
}
