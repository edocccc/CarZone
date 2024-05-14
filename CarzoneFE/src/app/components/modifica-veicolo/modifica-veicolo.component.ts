import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {VeicoloService} from "../../services/veicolo.service";
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";
import {MessageResponse} from "../../dto/response/MessageResponse";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-modifica-veicolo',
  templateUrl: './modifica-veicolo.component.html',
  styleUrls: ['./modifica-veicolo.component.css']
})
export class ModificaVeicoloComponent implements OnInit{
  veicolo!: ShowDettagliVeicoloResponse;
  constructor(private veicoloService: VeicoloService,private router: Router) { }

  ngOnInit() {
    this.veicoloService.getVeicolo(this.router.url.split('/')[2]).subscribe({
      next: (response) => {
        this.veicolo = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
  }

  modificaVeicolo(){
    this.veicoloService.modificaVeicolo(this.veicolo).subscribe({
      next: (response: MessageResponse) => {
        console.log(response);
        this.router.navigate(['gestioneVeicoli'])
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })

  }

  redirectGestioneVeicoli() {
    this.router.navigate(['gestioneVeicoli']);
  }
}
