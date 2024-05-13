import {Component, OnInit} from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";
import {ShowDettagliVeicoloManagerResponse} from "../../dto/response/ShowDettagliVeicoloManagerResponse";

@Component({
  selector: 'app-dettagli-veicolo',
  templateUrl: './dettagli-veicolo.component.html',
  styleUrls: ['./dettagli-veicolo.component.css']
})
export class DettagliVeicoloComponent implements OnInit{
  veicolo?: ShowDettagliVeicoloManagerResponse;

  constructor(private veicoloService: VeicoloService, private router: Router) { }

  ngOnInit(): void {
    const id: string = this.router.url.split('/')[2];

    this.veicoloService.getVeicolo(id).subscribe((veicolo: ShowDettagliVeicoloManagerResponse) => {
      console.log(veicolo);
      this.veicolo = veicolo;
    });
  }

  redirectPrenotazioneVeicolo() {
    this.router.navigate(['prenota/'+this.veicolo?.id]);
  }

  redirectHomepageCliente() {
    this.router.navigate(['homeCliente/']);
  }
}
