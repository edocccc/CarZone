import { Component } from '@angular/core';
import {AppuntamentoService} from "../../services/appuntamento.service";
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-registra-vendita',
  templateUrl: './registra-vendita.component.html',
  styleUrls: ['./registra-vendita.component.css']
})
export class RegistraVenditaComponent {
  idAppuntamento: number = +this.router.url.split('/')[2];
  venditaConclusa: boolean = false;

  constructor(private veicoloService: VeicoloService, private router: Router) { }


  registraVendita() {
    this.veicoloService.registraVendita(this.idAppuntamento, this.venditaConclusa).subscribe({
      next: () => {
        console.log('Esito della vendita registrato con successo');
        if (localStorage.getItem('ruolo') === 'DIPENDENTE') {
          this.router.navigate(['homeDipendente/' + localStorage.getItem('id')]);
        } else {
          this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
        }
      },
      error: (error: HttpErrorResponse) => {
        console.log('Errore durante la registrazione della vendita' + error.error);
      }
    });
  }

  redirectHomepage() {
    if (localStorage.getItem('ruolo') === 'DIPENDENTE') {
      this.router.navigate(['homeDipendente/' + localStorage.getItem('id')]);
    } else {
      this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
    }
  }
}
