import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrazioneComponent } from './components/registrazione/registrazione.component';
import { LoginComponent } from './components/login/login.component';
import {HomepageClienteComponent} from "./components/homepage-cliente/homepage-cliente.component";
import {DettagliVeicoloComponent} from "./components/dettagli-veicolo/dettagli-veicolo.component";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrazioneComponent },
  { path: 'homeCliente', component: HomepageClienteComponent },
  { path: 'dettagli', component: DettagliVeicoloComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
