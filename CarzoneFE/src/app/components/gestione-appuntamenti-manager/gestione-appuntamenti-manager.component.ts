import {Component, OnInit} from '@angular/core';
import {ShowAppuntamentoManagerResponse} from "../../dto/response/ShowAppuntamentoManagerResponse";
import {AppuntamentoService} from "../../services/appuntamento.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-gestione-appuntamenti-manager',
  templateUrl: './gestione-appuntamenti-manager.component.html',
  styleUrls: ['./gestione-appuntamenti-manager.component.css']
})
export class GestioneAppuntamentiManagerComponent implements OnInit{
  appuntamenti: ShowAppuntamentoManagerResponse[] = [];

  constructor(private appuntamentoService: AppuntamentoService, private router: Router) {  }

  ngOnInit(): void {
    this.getAllAppuntamentiManager();
  }

  getAllAppuntamentiManager() {
    this.appuntamentoService.getAllAppuntamentiManager().subscribe({
      next: (response: ShowAppuntamentoManagerResponse[] )  => {
        this.appuntamenti = response;
        console.log(response);
      },
      error: () => {
        alert('Errore durante il recupero degli appuntamenti');
      }
    });
  }

  redirectRegistraVendita(idAppuntamento: number) {
    this.router.navigate(['/registraVendita/'+idAppuntamento]);
  }

  modificaAppuntamento(idAppuntamento: number) {
    this.router.navigate(['/modificaAppuntamento/' + idAppuntamento]);
  }

  eliminaAppuntamento(id: number) {
    this.appuntamentoService.eliminaAppuntamento(id).subscribe({
      next: () => {
        this.getAllAppuntamentiManager();
      },
      error: () => {
        alert('Errore durante l\'eliminazione dell\'appuntamento');
      }
    });
  }

  redirectAggiungiAppuntamento() {
    this.router.navigateByUrl('/aggiungiAppuntamento');
  }
}
