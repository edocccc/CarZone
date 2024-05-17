import {Component, OnInit} from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {UtenteService} from "../../services/utente.service";
import {AppuntamentiDipendenteRequest} from "../../dto/request/AppuntamentiDipendenteRequest";
import {ShowAppuntamentoResponse} from "../../dto/response/ShowAppuntamentoResponse";
import {Router} from "@angular/router";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {ShowValutazioneMediaResponse} from "../../dto/response/ShowValutazioneMediaResponse";
import {ShowRecensioneResponse} from "../../dto/response/ShowRecensioneResponse";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-homepage-dipendente',
  templateUrl: './homepage-dipendente.component.html',
  styleUrls: ['./homepage-dipendente.component.css']
})
export class HomepageDipendenteComponent implements OnInit{
  appuntamentiDipendente: ShowAppuntamentoResponse[] = [];
  valutazioneMedia : number = 0.0;
  idDipendente: number = +this.router.url.split('/')[2];
  appuntamentiLiberi: ShowAppuntamentoResponse[] = [];
  oraAttuale: Date = new Date();
  recensioniDipendente: ShowRecensioneResponse[] = [];
  accessoEffettuato: boolean = !!localStorage.getItem('token');

  constructor(private veicoloService: VeicoloService, private utenteService: UtenteService, private appuntamentoService: AppuntamentoService, private router: Router) { }

  ngOnInit(): void {
    this.getAppuntamentiDipendente(this.idDipendente);
    this.getValutazioneMedia(this.idDipendente);
    this.getAppuntamentiLiberi();
    this.getRecensioniDipendente(this.idDipendente);
  }

  getAppuntamentiDipendente(idDipendente: number) {
    this.appuntamentoService.getAppuntamentiDipendente(idDipendente).subscribe({
      next: (response) => {
        this.appuntamentiDipendente = response;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error.error);
      }
    });
  }

  getValutazioneMedia(idDipendente: number) {
    this.appuntamentoService.getValutazioneMedia(idDipendente).subscribe({
      next: (response ) => {
        this.valutazioneMedia = response.valutazioneMedia;
      },
      error: (error: HttpErrorResponse) => {
        console.log('Errore durante il recupero della valutazione media' + error.error);
      }
    });

  }

  getAppuntamentiLiberi() {
    this.appuntamentoService.getAppuntamentiLiberi().subscribe({
      next: (response) => {
        this.appuntamentiLiberi = response;
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error.error);
      }
    });
  }

  prendiInCarico(idAppuntamento: number) {
    this.appuntamentoService.prendiInCarico(this.idDipendente, idAppuntamento).subscribe({
      next: () => {
        this.getAppuntamentiDipendente(this.idDipendente);
        this.getAppuntamentiLiberi();
      },
      error: (error: HttpErrorResponse) => {
        console.log("Si è verificato un errore:", error.error);
        alert('Errore durante il prendere in carico');
      }
    });
  }

  redirectRegistraVendita(idAppuntamento: number) {
    this.router.navigate(['/registraVendita/'+idAppuntamento]);
  }

  private getRecensioniDipendente(idDipendente: number) {
    this.appuntamentoService.getRecensioniDipendente(idDipendente).subscribe({
      next: (response) => {
        this.recensioniDipendente = response;
      },
      error: (error) => {
        console.log("Si è verificato un errore:", error.error);
      }
    });
  }

  logout() {
    this.utenteService.logout();
    this.accessoEffettuato = false;
    this.router.navigate(['homeCliente/']);
  }
}
