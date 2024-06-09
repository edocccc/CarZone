import {Component, OnInit} from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";

@Component({
  selector: 'app-dettagli-veicolo',
  templateUrl: './dettagli-veicolo.component.html',
  styleUrls: ['./dettagli-veicolo.component.css']
})
//classe che permette di visualizzare i dettagli di un veicolo al cliente e al guest
export class DettagliVeicoloComponent implements OnInit{
  //dichiarazione della variabile veicolo per visualizzare i dettagli del veicolo
  veicolo?: ShowDettagliVeicoloManagerResponse;
  //dichiarazione della variabile accessoEffettuato per verificare se l'utente è loggato
  accessoEffettuato: boolean = !!localStorage.getItem('token');

  //costruttore che inizializza il service necessario e il router per la navigazione
  constructor(private veicoloService: VeicoloService, private router: Router) { }

  //metodo che specifica cosa fare all'inizializzazione della classe
  //richiama il metodo getVeicolo del servizio veicoloService passandogli l'id del veicolo da visualizzare
  //l'id viene ottenuto dall'url
  ngOnInit(): void {
    const id: string = this.router.url.split('/')[2];

    this.veicoloService.getVeicolo(id).subscribe((veicolo: ShowDettagliVeicoloManagerResponse) => {
      //salva il veicolo nella variabile veicolo e lo stampa in console e lo stampa in console
      console.log(veicolo);
      this.veicolo = veicolo;
    });
  }

  //metodo che permette di reindirizzare alla pagina di prenotazione del veicolo se è disponibile
  redirectPrenotazioneVeicolo() {
    this.router.navigate(['prenota/'+this.veicolo?.id]);
  }

  //metodo che permette di reindirizzare alla homepage del cliente
  redirectHomepageCliente() {
    this.router.navigate(['homeCliente/']);
  }
}
