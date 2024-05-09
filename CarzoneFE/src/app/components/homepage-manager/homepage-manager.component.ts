import {Component, OnInit} from '@angular/core';
import {ShowValutazioniDipendenti} from "../../dto/response/ShowValutazioniDipendenti";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-homepage-manager',
  templateUrl: './homepage-manager.component.html',
  styleUrls: ['./homepage-manager.component.css']
})
export class HomepageManagerComponent implements OnInit{
  dipendentiConRecensioni: ShowValutazioniDipendenti[] = [];

  constructor(private appuntamentoService: AppuntamentoService, private router: Router) { }

  ngOnInit(): void {
    this.getDipendentiConRecensioni();
  }

  getDipendentiConRecensioni() {
    this.appuntamentoService.getDipendentiConRecensioni().subscribe({
      next: (response: ShowValutazioniDipendenti[]) => {
        this.dipendentiConRecensioni = response;
        console.log(response);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }}
    );
  }

  redirectGestioneVeicoli() {
    this.router.navigate(['gestioneVeicoli']);
  }

  redirectGestioneUtenti() {
    this.router.navigate(['gestioneUtenti']);
  }
}
