import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {HttpErrorResponse} from "@angular/common/http";
import {MessageResponse} from "../../dto/response/MessageResponse";

@Component({
  selector: 'app-lascia-recensione',
  templateUrl: './lascia-recensione.component.html',
  styleUrls: ['./lascia-recensione.component.css']
})
export class LasciaRecensioneComponent {
  idAppuntamento: number = +this.router.url.split('/')[2];
  votoRecensione: number = 0;
  testoRecensione: string = '';

  constructor(private appuntamentoService: AppuntamentoService, private router: Router) { }


  inviaRecensione() {
    this.appuntamentoService.inviaRecensione(this.idAppuntamento, this.votoRecensione, this.testoRecensione).subscribe({
      next: (response: MessageResponse) => {
        this.router.navigate(['mieiAppuntamenti/'+ localStorage.getItem('id')]);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
  }

  redirectMieiAppuntamenti() {
    this.router.navigate(['mieiAppuntamenti/' + localStorage.getItem('id')]);
  }
}
