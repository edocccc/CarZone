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
import {ShowAppuntamentoModificaResponse} from "../../dto/response/ShowAppuntamentoModificaResponse";

@Component({
  selector: 'app-modifica-appuntamento',
  templateUrl: './modifica-appuntamento.component.html',
  styleUrls: ['./modifica-appuntamento.component.css']
})
export class ModificaAppuntamentoComponent implements OnInit{
  appuntamento!: ShowAppuntamentoModificaResponse;
  idAppuntamento: number = +this.router.url.split('/')[2];

  veicoli: ShowDettagliVeicoloManagerResponse[] = [];
  clienti: ShowUtenteManagerResponse[] = [];
  dipendenti: ShowUtenteManagerResponse[] = [];
  constructor(private utenteService: UtenteService, private veicoloService: VeicoloService, private appuntamentoService: AppuntamentoService , private router: Router) { }

  ngOnInit(): void {
    this.getAppuntamento(this.idAppuntamento);
    this.getClienti();
    this.getDipendenti();
    this.getVeicoliDisponibiliESelezionato(this.idAppuntamento);
  }

  getAppuntamento(idAppuntamento: number) {
    this.appuntamentoService.getAppuntamento(idAppuntamento).subscribe({
      next: (response: ShowAppuntamentoModificaResponse) => {
        this.appuntamento = response;
        console.log("appuntamento trovato: "+response);
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error.error);
      },
    });
  }

  modificaAppuntamento() {
    this.appuntamentoService.modificaAppuntamento(this.idAppuntamento, this.appuntamento.dataOra, this.appuntamento.idVeicolo, this.appuntamento.idCliente, this.appuntamento.idDipendente).subscribe({
      next: (response:MessageResponse) => {
        this.router.navigate(['gestioneAppuntamenti']);
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error.error);
      },
    });
  }

  getVeicoliDisponibiliESelezionato(idAppuntamento: number) {
    this.veicoloService.getVeicoliDisponibiliESelezionato(idAppuntamento).subscribe({
      next: (response: ShowDettagliVeicoloManagerResponse[]) => {
        this.veicoli = response;
        console.log("veicoli disponibili e selzionato:" + response);
        console.log(this.veicoli)
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error.error);
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
