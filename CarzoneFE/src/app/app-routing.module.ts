import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrazioneComponent } from './components/registrazione/registrazione.component';
import { LoginComponent } from './components/login/login.component';
import {HomepageClienteComponent} from "./components/homepage-cliente/homepage-cliente.component";
import {DettagliVeicoloComponent} from "./components/dettagli-veicolo/dettagli-veicolo.component";
import {HomepageManagerComponent} from "./components/homepage-manager/homepage-manager.component";
import {HomepageDipendenteComponent} from "./components/homepage-dipendente/homepage-dipendente.component";
import {PrenotazioneVeicoloComponent} from "./components/prenotazione-veicolo/prenotazione-veicolo.component";

const routes: Routes = [
  { path: '', redirectTo: '/homeCliente', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrazioneComponent },
  { path: 'homeCliente', component: HomepageClienteComponent },
  { path: 'homeManager/:id', component: HomepageManagerComponent },
  { path: 'homeDipendente/:id', component: HomepageDipendenteComponent },
  { path: 'dettagli/:id', component: DettagliVeicoloComponent},
  { path: 'prenota/:id', component: PrenotazioneVeicoloComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
