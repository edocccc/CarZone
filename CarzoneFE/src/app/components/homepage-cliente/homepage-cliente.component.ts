import { Component } from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {Veicolo} from "../../dto/response/ShowVeicoliResponse";

@Component({
  selector: 'app-homepage-cliente',
  templateUrl: './homepage-cliente.component.html',
  styleUrls: ['./homepage-cliente.component.css']
})
export class HomepageClienteComponent {
  veicoli: Veicolo[] = [];

  constructor(private veicoloService: VeicoloService) { }

  ngOnInit(): void {
    this.getVeicoli();
  }

  getVeicoli(): void {
    this.veicoloService
      .getVeicoli()
      .subscribe({
        next: (response) => {
          this.veicoli = response.veicoli;
          console.log(response.veicoli)
        },
        error: (error) => {
          console.log(error.error.message);
        },
      });
  }
}
