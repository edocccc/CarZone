import {Component, OnInit} from '@angular/core';
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";
import {VeicoloService} from "../../services/veicolo.service";
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";
import {Router} from "@angular/router";

@Component({
  selector: 'app-gestione-veicoli-manager',
  templateUrl: './gestione-veicoli-manager.component.html',
  styleUrls: ['./gestione-veicoli-manager.component.css']
})
export class GestioneVeicoliManagerComponent implements OnInit{
  veicoli: ShowDettagliVeicoloManagerResponse[] = [];
  constructor(private veicoloService: VeicoloService, private router: Router) { }

  ngOnInit(): void {
    this.getAllVeicoli();
  }

  private getAllVeicoli() {
    this.veicoloService.getAllVeicoliConDettagli().subscribe({
      next: (response: ShowDettagliVeicoloManagerResponse[] )  => {
        this.veicoli = response;
        console.log(response);
      },
      error: () => {
        alert('Errore durante il recupero dei veicoli');
      }
    });
  }

  eliminaVeicolo(id: number) {
    this.veicoloService.eliminaVeicolo(id).subscribe({
      next: () => {
        this.getAllVeicoli();
      },
      error: () => {
        alert('Errore durante l\'eliminazione del veicolo');
      }
    });
  }

  redirectModificaVeicolo(id: number) {
    this.router.navigate(['modificaVeicolo/'+id]);
  }

  redirectAggiungiVeicolo() {
    this.router.navigate(['aggiungiVeicolo']);
  }

  redirectHomepageManager() {
    this.router.navigate(['homeManager/' + localStorage.getItem('id')]);
  }
}
