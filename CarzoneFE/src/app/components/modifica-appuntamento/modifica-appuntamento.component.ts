import {Component, OnInit} from '@angular/core';
import {ShowAppuntamentoManagerResponse} from "../../dto/response/ShowAppuntamentoManagerResponse";
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";
import {ShowUtenteManagerResponse} from "../../dto/response/ShowUtenteManagerResponse";
import {UtenteService} from "../../services/utente.service";
import {VeicoloService} from "../../services/veicolo.service";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {MessageResponse} from "../../dto/response/MessageResponse";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-modifica-appuntamento',
  templateUrl: './modifica-appuntamento.component.html',
  styleUrls: ['./modifica-appuntamento.component.css']
})
export class ModificaAppuntamentoComponent implements OnInit{
  idAppuntamento: number = +this.router.url.split('/')[2];

  dataOra: Date = new Date();
  idVeicolo: number = 0;
  idCliente: number = 0;
  idDipendente: number = 0;

  veicoli: ShowDettagliVeicoloManagerResponse[] = [];
  clienti: ShowUtenteManagerResponse[] = [];
  dipendenti: ShowUtenteManagerResponse[] = [];
  constructor(private utenteService: UtenteService, private veicoloService: VeicoloService, private appuntamentoService: AppuntamentoService , private router: Router) { }

  ngOnInit(): void {
    this.getVeicoliDisponibili();
    this.getClienti();
    this.getDipendenti();
  }

  modificaAppuntamento() {
    this.appuntamentoService.modificaAppuntamento(this.idAppuntamento,this.dataOra, this.idVeicolo, this.idCliente, this.idDipendente).subscribe({
      next: (response:MessageResponse) => {
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error);
      },
    });
  }

  getVeicoliDisponibili() {
    this.veicoloService.getVeicoliDisponibili().subscribe({
      next: (response: ShowDettagliVeicoloManagerResponse[]) => {
        this.veicoli = response;
        console.log(response);
        console.log(this.veicoli)
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error);
      },
    });
  }

  getClienti() {
    this.utenteService.getClienti().subscribe({
      next: (response: ShowUtenteManagerResponse[]) => {
        this.clienti = response;
        console.log(response);
        console.log(this.clienti)
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error.message);
      },
    });
  }

  private getDipendenti() {
    this.utenteService.getDipendenti().subscribe({
      next: (response: ShowUtenteManagerResponse[]) => {
        this.dipendenti = response;
        console.log(response);
        console.log(this.dipendenti)
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error.message);
      },
    });
  }

  redirectGestioneAppuntamenti() {
    this.router.navigate(['gestioneAppuntamenti' ]);
  }
}
