import {Component, OnInit} from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {ShowVeicoloResponse} from "../../dto/response/ShowVeicoloResponse";
import {UtenteService} from "../../services/utente.service";

@Component({
  selector: 'app-homepage-cliente',
  templateUrl: './homepage-cliente.component.html',
  styleUrls: ['./homepage-cliente.component.css']
})
export class HomepageClienteComponent implements OnInit{
  veicoli: ShowVeicoloResponse[] = [];
  accessoEffettuato: boolean = !!localStorage.getItem('token');
  statoVeicolo: string = '';

  constructor(private veicoloService: VeicoloService, private utenteService: UtenteService) { }

  ngOnInit(): void {
    this.getVeicoli();
    this.accessoEffettuato = this.utenteService.accessoEffettuato();
  }

  getVeicoli(): void {
    this.veicoloService.getVeicoli().subscribe({
      next: (response) => {
        this.veicoli = response;
        console.log(response);
        console.log(this.veicoli)
      },
      error: (error) => {
        console.log("Si è verificato un errore:", error);
      },
    });
  }

  logout(): void {
    this.utenteService.logout();
    this.accessoEffettuato = false;
  }
}
