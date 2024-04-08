import { Injectable } from '@angular/core';
import { globalBackEndUrl } from 'environment';
import { HttpClient } from '@angular/common/http';
import { RegisterRequest } from '../dto/request/RegisterRequest';
import { Observable } from 'rxjs';
import { MessageResponse } from '../dto/response/MessageResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private backEndUrl: string = globalBackEndUrl + 'utente/';

  constructor(private http: HttpClient) {}

  registra(
    email: string,
    dataNascita: Date,
    username: string,
    password: string
  ): Observable<MessageResponse> {
    const request: RegisterRequest = { email, dataNascita, username, password };
    return this.http.post<MessageResponse>(this.backEndUrl + 'signup', request);
  }
}
