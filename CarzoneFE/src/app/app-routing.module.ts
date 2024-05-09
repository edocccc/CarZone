import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrazioneComponent } from './components/registrazione/registrazione.component';
import { LoginComponent } from './components/login/login.component';
import {HomepageClienteComponent} from "./components/homepage-cliente/homepage-cliente.component";
import {DettagliVeicoloComponent} from "./components/dettagli-veicolo/dettagli-veicolo.component";
import {HomepageManagerComponent} from "./components/homepage-manager/homepage-manager.component";
import {HomepageDipendenteComponent} from "./components/homepage-dipendente/homepage-dipendente.component";
import {PrenotazioneVeicoloComponent} from "./components/prenotazione-veicolo/prenotazione-veicolo.component";
import {AutenticazioneGuard} from "./guards/autenticazione.guard";
import {RicercaComponent} from "./components/ricerca/ricerca.component";
import {RegistraVenditaComponent} from "./components/registra-vendita/registra-vendita.component";
import {
  GestioneVeicoliManagerComponent
} from "./components/gestione-veicoli-manager/gestione-veicoli-manager.component";
import {ModificaVeicoloComponent} from "./components/modifica-veicolo/modifica-veicolo.component";
import {AggiungiVeicoloComponent} from "./components/aggiungi-veicolo/aggiungi-veicolo.component";
import {GestioneUtentiManagerComponent} from "./components/gestione-utenti-manager/gestione-utenti-manager.component";
import {ModificaUtenteComponent} from "./components/modifica-utente/modifica-utente.component";
import {AggiungiDipendenteComponent} from "./components/aggiungi-dipendente/aggiungi-dipendente.component";

const routes: Routes = [
  { path: '', redirectTo: '/homeCliente', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrazioneComponent },
  { path: 'homeCliente', component: HomepageClienteComponent },
  { path: 'homeManager/:id', component: HomepageManagerComponent },
  { path: 'homeDipendente/:id', component: HomepageDipendenteComponent },
  { path: 'dettagli/:id', component: DettagliVeicoloComponent },
  { path: 'prenota/:id', component: PrenotazioneVeicoloComponent , canActivate: [AutenticazioneGuard]},
  { path: 'cerca', component: RicercaComponent },
  { path: 'registraVendita/:id', component: RegistraVenditaComponent },
  { path: 'gestioneVeicoli', component: GestioneVeicoliManagerComponent },
  { path: 'modificaVeicolo/:id', component: ModificaVeicoloComponent },
  { path: 'aggiungiVeicolo', component: AggiungiVeicoloComponent },
  { path: 'gestioneUtenti', component: GestioneUtentiManagerComponent },
  { path: 'modificaUtente/:id', component: ModificaUtenteComponent },
  { path: 'registraDipendente', component: AggiungiDipendenteComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
