import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {ShowAppuntamentoResponse} from "../../dto/response/ShowAppuntamentoResponse";
import {ShowAppuntamentoManagerResponse} from "../../dto/response/ShowAppuntamentoManagerResponse";
import {ShowAppuntamentoConRecensioneResponse} from "../../dto/response/ShowAppuntamentoConRecensioneResponse";
import {ShowRecensioniClienteResponse} from "../../dto/response/ShowRecensioniClienteResponse";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-miei-appuntamenti',
  templateUrl: './miei-appuntamenti.component.html',
  styleUrls: ['./miei-appuntamenti.component.css']
})
export class MieiAppuntamentiComponent implements OnInit{
  idCliente: number = +this.router.url.split('/')[2];
  appuntamenti: ShowAppuntamentoConRecensioneResponse[] = [];
  recensioni: ShowRecensioniClienteResponse[] = [];

  constructor(private appuntamentoService: AppuntamentoService, private router: Router) { }

  ngOnInit(): void {
    this.getAllAppuntamentiCliente();
    this.getAllRecensioniCliente();
  }

  getAllAppuntamentiCliente() {
    this.appuntamentoService.getAllAppuntamentiCliente(this.idCliente).subscribe({
      next: (response: ShowAppuntamentoConRecensioneResponse[] )  => {
        this.appuntamenti = response;
        console.log(response);
      },
      error: () => {
        alert('Errore durante il recupero degli appuntamenti');
      }
    });
  }

  redirectLasciaRecensione(idAppuntamento: number) {
    this.router.navigate(['/lasciaRecensione/'+idAppuntamento]);
  }

  private getAllRecensioniCliente() {
    this.appuntamentoService.getAllRecensioniCliente(this.idCliente).subscribe({
      next: (response: ShowRecensioniClienteResponse[] )  => {
        this.recensioni = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error.error);
      }
    });
  }
}
