import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable } from 'rxjs';
import { UtenteService } from '../services/utente.service';
import { ToastrService } from 'ngx-toastr';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AutenticazioneGuard implements CanActivate {
  constructor(
    private utenteService: UtenteService,
    private router: Router,
    private toastr: ToastrService // Aggiungi ToastrService qui
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | boolean {
    return this.utenteService.checkIsAuthenticated().pipe(
      map(isAuthenticated => {
        if (!isAuthenticated) {
          console.log(isAuthenticated);
          this.toastr.error('Devi essere autenticato per accedere a questa pagina'); // Mostra il messaggio toastr
          this.router.navigate(['/login']);
          return false;
        }
        console.log(isAuthenticated);
        return true;
      })
    );
  }
}
