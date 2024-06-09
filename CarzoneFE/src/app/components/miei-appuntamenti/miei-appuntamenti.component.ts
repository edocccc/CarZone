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
//classe che permette di visualizzare i miei appuntamenti e le recensioni del cliente
export class MieiAppuntamentiComponent implements OnInit{
  //dichiarazione della variabile idCliente ottenuto dall'url
  idCliente: number = +this.router.url.split('/')[2];
  //dichiarazione della lista di appuntamenti da visualizzare
  appuntamenti: ShowAppuntamentoConRecensioneResponse[] = [];
  //dichiarazione della lista di recensioni del cliente
  recensioni: ShowRecensioniClienteResponse[] = [];

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private appuntamentoService: AppuntamentoService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama i metodi per ottenere gli appuntamenti e le recensioni del cliente da questa classe
  ngOnInit(): void {
    this.getAllAppuntamentiCliente();
    this.getAllRecensioniCliente();
  }

  //metodo che permette di ottenere tutti gli appuntamenti del cliente
  //richiama il metodo getAllAppuntamentiCliente del servizio appuntamentoService passandogli l'id del cliente
  getAllAppuntamentiCliente() {
    this.appuntamentoService.getAllAppuntamentiCliente(this.idCliente).subscribe({
      next: (response: ShowAppuntamentoConRecensioneResponse[] )  => {
        //se la richiesta va a buon fine, salva gli appuntamenti nella variabile appuntamenti e li stampa in console
        this.appuntamenti = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error.error);
      }
    });
  }

  //metodo che permette di reindirizzare alla pagina di lascia recensione
  redirectLasciaRecensione(idAppuntamento: number) {
    this.router.navigate(['/lasciaRecensione/'+idAppuntamento]);
  }

  //metodo che permette di ottenere tutte le recensioni del cliente
  //richiama il metodo getAllRecensioniCliente del servizio appuntamentoService passandogli l'id del cliente
  private getAllRecensioniCliente() {
    this.appuntamentoService.getAllRecensioniCliente(this.idCliente).subscribe({
      next: (response: ShowRecensioniClienteResponse[] )  => {
        //se la richiesta va a buon fine, salva le recensioni nella variabile recensioni e le stampa in console
        this.recensioni = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        //se si verifica un errore, lo stampa in console
        console.log(error.error);
      }
    });
  }

  //metodo che permette di reindirizzare alla homepage del cliente
  redirectHomepageCliente() {
    this.router.navigate(['homeCliente/']);
  }
}
