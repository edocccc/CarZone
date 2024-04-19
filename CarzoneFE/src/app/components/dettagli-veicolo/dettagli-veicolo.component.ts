import {Component, OnInit} from '@angular/core';
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";

@Component({
  selector: 'app-dettagli-veicolo',
  templateUrl: './dettagli-veicolo.component.html',
  styleUrls: ['./dettagli-veicolo.component.css']
})
export class DettagliVeicoloComponent implements OnInit{
  veicolo!: ShowDettagliVeicoloResponse;

  constructor(private veicoloService: VeicoloService, private router: Router) { }

  ngOnInit(): void {
    const id: string = this.router.url.split('/')[2];

    this.veicoloService.getVeicolo(id).subscribe((veicolo) => {
      console.log(veicolo);
      this.veicolo = veicolo;
    });
  }

}
