import { Component } from '@angular/core';
import {AppuntamentoService} from "../../services/appuntamento.service";

@Component({
  selector: 'app-registra-vendita',
  templateUrl: './registra-vendita.component.html',
  styleUrls: ['./registra-vendita.component.css']
})
export class RegistraVenditaComponent {
  venditaConclusa: boolean = false;

  constructor(private appuntamentoService: AppuntamentoService) { }


  registraVendita() {

  }
}
