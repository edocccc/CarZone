import {Component, OnInit} from '@angular/core';
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {ShowUtenteManagerResponse} from "../../dto/response/ShowUtenteManagerResponse";
import {UtenteService} from "../../services/utente.service";

@Component({
  selector: 'app-gestione-utenti-manager',
  templateUrl: './gestione-utenti-manager.component.html',
  styleUrls: ['./gestione-utenti-manager.component.css']
})
export class GestioneUtentiManagerComponent implements OnInit{
  utenti: ShowUtenteManagerResponse[] = [];
  constructor(private utenteService: UtenteService, private router: Router) { }

  ngOnInit(): void {
    this.getAllUtentiManager();
  }

  getAllUtentiManager() {
    this.utenteService.getAllUtentiManager().subscribe({
      next: (response: ShowUtenteManagerResponse[] )  => {
        this.utenti = response;
        console.log(response);
      },
      error: () => {
        alert('Errore durante il recupero degli utenti');
      }
    });
  }

  eliminaUtente(id: number) {
    this.utenteService.eliminaUtente(id).subscribe({
      next: () => {
        this.getAllUtentiManager();
      },
      error: () => {
        alert('Errore durante l\'eliminazione dell\'utente');
      }
    });
  }

  redirectModificaUtente(id: number) {
    this.router.navigate(['modificaUtente/'+id]);
  }

  redirectAggiungiDipendente() {
    this.router.navigate(['registraDipendente']);
  }
}
