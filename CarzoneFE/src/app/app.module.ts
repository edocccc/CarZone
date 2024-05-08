import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrazioneComponent } from './components/registrazione/registrazione.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HomepageClienteComponent } from './components/homepage-cliente/homepage-cliente.component';
import { DettagliVeicoloComponent } from './components/dettagli-veicolo/dettagli-veicolo.component';
import { HomepageDipendenteComponent } from './components/homepage-dipendente/homepage-dipendente.component';
import { HomepageManagerComponent } from './components/homepage-manager/homepage-manager.component';
import { PrenotazioneVeicoloComponent } from './components/prenotazione-veicolo/prenotazione-veicolo.component';
import { RicercaComponent } from './components/ricerca/ricerca.component';
import { RegistraVenditaComponent } from './components/registra-vendita/registra-vendita.component';
import { GestioneVeicoliManagerComponent } from './components/gestione-veicoli-manager/gestione-veicoli-manager.component';
import { ModificaVeicoloComponent } from './components/modifica-veicolo/modifica-veicolo.component';
import { AggiungiVeicoloComponent } from './components/aggiungi-veicolo/aggiungi-veicolo.component';

@NgModule({
  declarations: [AppComponent, LoginComponent, RegistrazioneComponent, HomepageClienteComponent, DettagliVeicoloComponent, HomepageDipendenteComponent, HomepageManagerComponent, PrenotazioneVeicoloComponent, RicercaComponent, RegistraVenditaComponent, GestioneVeicoliManagerComponent, ModificaVeicoloComponent, AggiungiVeicoloComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
