import {Component, OnInit} from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {UtenteService} from "../../services/utente.service";
import {AppuntamentiDipendenteRequest} from "../../dto/request/AppuntamentiDipendenteRequest";
import {ShowAppuntamentoResponse} from "../../dto/response/ShowAppuntamentoResponse";
import {Router} from "@angular/router";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {ShowValutazioneMediaResponse} from "../../dto/response/ShowValutazioneMediaResponse";

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

  constructor(private veicoloService: VeicoloService, private utenteService: UtenteService, private appuntamentoService: AppuntamentoService, private router: Router) { }

  ngOnInit(): void {
    this.getAppuntamentiDipendente(this.idDipendente);
    this.getValutazioneMedia(this.idDipendente);
    this.getAppuntamentiLiberi();
  }

  getAppuntamentiDipendente(idDipendente: number) {
    this.appuntamentoService.getAppuntamentiDipendente(idDipendente).subscribe({
      next: (response) => {
        this.appuntamentiDipendente = response;
      },
      error: () => {
        alert('Errore durante il recupero degli appuntamenti');
      }
    });
  }

  getValutazioneMedia(idDipendente: number) {
    this.appuntamentoService.getValutazioneMedia(idDipendente).subscribe({
      next: (response ) => {
        this.valutazioneMedia = response.valutazioneMedia;
      },
      error: () => {
        alert('Errore durante il recupero della valutazione media');
      }
    });

  }

  getAppuntamentiLiberi() {
    this.appuntamentoService.getAppuntamentiLiberi().subscribe({
      next: (response) => {
        this.appuntamentiLiberi = response;
      },
      error: (error) => {
        console.log("Si è verificato un errore:", error.error);
      }
    });
  }

  prendiInCarico(idAppuntamento: number) {
    this.appuntamentoService.prendiInCarico(this.idDipendente, idAppuntamento).subscribe({
      next: () => {
        this.getAppuntamentiDipendente(this.idDipendente);
        alert('Appuntamento preso in carico con successo');
      },
      error: () => {
        alert('Errore durante il prendere in carico');
      }
    });
  }

  redirectRegistraVendita(idAppuntamento: number) {
    this.router.navigate(['/registraVendita/'+idAppuntamento]);
  }
}
