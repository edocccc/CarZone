import {Component, OnInit} from '@angular/core';
import {ShowAppuntamentoManagerResponse} from "../../dto/response/ShowAppuntamentoManagerResponse";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-gestione-appuntamenti-manager',
  templateUrl: './gestione-appuntamenti-manager.component.html',
  styleUrls: ['./gestione-appuntamenti-manager.component.css']
})
//classe che permette di visualizzare e gestire gli appuntamenti del manager
export class GestioneAppuntamentiManagerComponent implements OnInit{
  //dichiarazione della lista di appuntamenti da visualizzare
  appuntamenti: ShowAppuntamentoManagerResponse[] = [];

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private appuntamentoService: AppuntamentoService, private router: Router) {  }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getAllAppuntamentiManager di questa classe
  ngOnInit(): void {
    this.getAllAppuntamentiManager();
  }

  //metodo che permette di ottenere tutti gli appuntamenti per il manager
  //richiama il metodo getAllAppuntamentiManager del servizio appuntamentoService
  getAllAppuntamentiManager() {
    this.appuntamentoService.getAllAppuntamentiManager().subscribe({
      next: (response: ShowAppuntamentoManagerResponse[] )  => {
        //se la richiesta va a buon fine, salva gli appuntamenti nella variabile appuntamenti e li stampa in console
        this.appuntamenti = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante il recupero degli appuntamenti: ' + error.message);
      }
    });
  }

  //metodo che permette di reindirizzare alla pagina di registrazione della vendita
  redirectRegistraVendita(idAppuntamento: number) {
    this.router.navigate(['/registraVendita/'+idAppuntamento]);
  }

  //metodo che permette di reindirizzare alla pagina di modifica dell'appuntamento
  modificaAppuntamento(idAppuntamento: number) {
    this.router.navigate(['/modificaAppuntamento/' + idAppuntamento]);
  }

  //metodo che permette di eliminare un appuntamento
  //richiama il metodo eliminaAppuntamento del servizio appuntamentoService passandogli l'id dell'appuntamento da eliminare
  eliminaAppuntamento(id: number) {
    this.appuntamentoService.eliminaAppuntamento(id).subscribe({

      next: () => {
        //se l'eliminazione va a buon fine, richiama il metodo per ottenere tutti gli appuntamenti
        this.getAllAppuntamentiManager();
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log('Errore durante l\'eliminazione dell\'appuntamento' + error.message);
      }
    });
  }

  //metodo che permette di reindirizzare alla pagina di aggiunta di un appuntamento
  redirectAggiungiAppuntamento() {
    this.router.navigateByUrl('/aggiungiAppuntamento');
  }

  //metodo che permette di reindirizzare alla homepage del manager
  redirectHomepageManager() {
    this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
  }
}
