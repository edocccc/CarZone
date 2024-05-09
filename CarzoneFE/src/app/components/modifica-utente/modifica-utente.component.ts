import {Component, OnInit} from '@angular/core';
import {ShowDettagliVeicoloResponse} from "../../dto/response/ShowDettagliVeicoloResponse";
import {VeicoloService} from "../../services/veicolo.service";
import {Router} from "@angular/router";
import {MessageResponse} from "../../dto/response/MessageResponse";
import {HttpErrorResponse} from "@angular/common/http";
import {ShowUtenteManagerResponse} from "../../dto/response/ShowUtenteManagerResponse";
import {UtenteService} from "../../services/utente.service";

@Component({
  selector: 'app-modifica-utente',
  templateUrl: './modifica-utente.component.html',
  styleUrls: ['./modifica-utente.component.css']
})
export class ModificaUtenteComponent implements OnInit{
  utente!: ShowUtenteManagerResponse;
  constructor(private utenteService: UtenteService, private router: Router) { }

  ngOnInit() {
    this.utenteService.getUtente(+this.router.url.split('/')[2]).subscribe({
      next: (response) => {
        this.utente = response;
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  modificaUtente(){
    this.utenteService.modificaUtente(this.utente).subscribe({
      next: (response: MessageResponse) => {
        console.log(response);
        this.router.navigate(['gestioneUtenti'])
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })

  }
}
