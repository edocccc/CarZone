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
//classe che permette di visualizzare la homepage del dipendente e le informazioni che contiene
export class HomepageDipendenteComponent implements OnInit{
  //dichiarazione della lista di appuntamenti del dipendente
  appuntamentiDipendente: ShowAppuntamentoResponse[] = [];
  //dichiarazione della variabile valutazioneMedia per visualizzare la valutazione media del dipendente
  valutazioneMedia : number = 0.0;
  //dichiarazione della variabile idDipendente ottento dall'url
  idDipendente: number = +this.router.url.split('/')[2];
  //dichiarazione della lista di appuntamenti liberi
  appuntamentiLiberi: ShowAppuntamentoResponse[] = [];
  //dichiarazione della lista di recensioni del dipendente
  recensioniDipendente: ShowRecensioneResponse[] = [];
  //dichiarazione della variabile accessoEffettuato per verificare se l'utente è loggato
  accessoEffettuato: boolean = !!localStorage.getItem('token');

  //costruttore che inizializza i service necessario e il router per la navigazione
  constructor(private veicoloService: VeicoloService, private utenteService: UtenteService, private appuntamentoService: AppuntamentoService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama i metodi per ottenere gli appuntamenti del dipendente, la valutazione media, gli appuntamenti liberi e le recensioni del dipendente da questa classe
  ngOnInit(): void {
    this.getAppuntamentiDipendente(this.idDipendente);
    this.getValutazioneMedia(this.idDipendente);
    this.getAppuntamentiLiberi();
    this.getRecensioniDipendente(this.idDipendente);
  }

  //metodo che permette di ottenere gli appuntamenti del dipendente
  //richiama il metodo getAppuntamentiDipendente del servizio appuntamentoService passandogli l'id del dipendente
  getAppuntamentiDipendente(idDipendente: number) {
    this.appuntamentoService.getAppuntamentiDipendente(idDipendente).subscribe({
      next: (response) => {
        //se la richiesta va a buon fine, salva gli appuntamenti nella variabile appuntamentiDipendente
        this.appuntamentiDipendente = response;
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error.error);
      }
    });
  }

  //metodo che permette di ottenere la valutazione media del dipendente
  //richiama il metodo getValutazioneMedia del servizio appuntamentoService passandogli l'id del dipendente
  getValutazioneMedia(idDipendente: number) {
    this.appuntamentoService.getValutazioneMedia(idDipendente).subscribe({
      next: (response ) => {
        //se la richiesta va a buon fine, salva la valutazione media nella variabile valutazioneMedia
        this.valutazioneMedia = response.valutazioneMedia;
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante il recupero della valutazione media' + error.error);
      }
    });

  }

  //metodo che permette di ottenere gli appuntamenti liberi
  //richiama il metodo getAppuntamentiLiberi del servizio appuntamentoService
  getAppuntamentiLiberi() {
    this.appuntamentoService.getAppuntamentiLiberi().subscribe({
      next: (response) => {
        //se la richiesta va a buon fine, salva gli appuntamenti liberi nella variabile appuntamentiLiberi
        this.appuntamentiLiberi = response;
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.error);
      }
    });
  }

  //metodo che permette di prendere in carico un appuntamento
  //richiama il metodo prendiInCarico del service appuntamentoService passandogli l'id del dipendente e l'id dell'appuntamento
  prendiInCarico(idAppuntamento: number) {
    this.appuntamentoService.prendiInCarico(this.idDipendente, idAppuntamento).subscribe({
      next: () => {
        //se la richiesta va a buon fine, richiama i metodi per ottenere gli appuntamenti del dipendente e gli appuntamenti liberi
        this.getAppuntamentiDipendente(this.idDipendente);
        this.getAppuntamentiLiberi();
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.error);
      }
    });
  }

  //metodo che permette di reindirizzare alla pagina di registrazione della vendita
  redirectRegistraVendita(idAppuntamento: number) {
    this.router.navigate(['/registraVendita/'+idAppuntamento]);
  }

  //metodo che permette di ottentere le recensioni del dipendente
  //richiama il metodo getRecensioniDipendente del service appuntamentoService passandogli l'id del dipendente
  private getRecensioniDipendente(idDipendente: number) {
    this.appuntamentoService.getRecensioniDipendente(idDipendente).subscribe({
      next: (response) => {
        //se la richiesta va a buon fine, salva le recensioni nella variabile recensioniDipendente
        this.recensioniDipendente = response;
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log("Si è verificato un errore:", error.error);
      }
    });
  }

  //metodo che permette di effettuare il logout
  //richiama il metodo logout del service utenteService
  logout() {
    this.utenteService.logout();
    this.accessoEffettuato = false;
    this.router.navigate(['homeCliente/']);
  }
}
