import { Component } from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {ShowVeicoloResponse} from "../../dto/response/ShowVeicoloResponse";

@Component({
  selector: 'app-homepage-cliente',
  templateUrl: './homepage-cliente.component.html',
  styleUrls: ['./homepage-cliente.component.css']
})
export class HomepageClienteComponent {
  veicoli: ShowVeicoloResponse[] = [];

  constructor(private veicoloService: VeicoloService) { }

  ngOnInit(): void {
    this.getVeicoli();
  }

  getVeicoli(): void {
    this.veicoloService.getVeicoli().subscribe({
      next: (response) => {
        this.veicoli = response;
        console.log("inizio del componente");
        console.log(response);
        console.log(this.veicoli)
        console.log("fine del componente");
      },
      error: (error) => {
        console.log("Si è verificato un errore:", error);
      },
    });
  }
}
