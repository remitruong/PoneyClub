import { Injectable } from '@angular/core';
import {HTTP_INTERCEPTORS, HttpErrorResponse} from '@angular/common/http';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import {catchError} from "rxjs/operators";
import {Observable, of, throwError} from "rxjs";
import {Router} from "@angular/router";
import {AuthenticationService} from "../services/authentification.service";
import {AlertService} from "../services/alert.service";


const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor{

    constructor(private token: TokenStorageService,private authentificationService: AuthenticationService, private router: Router, private alertService: AlertService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        let authReq = req;
        const token = this.token.getToken();
        if (token != null) {
          authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, token)});
          //authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, token) });
        }
        return next.handle(authReq).pipe(catchError(x=> this.handleAuthError(x)));
    }

  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    if (err.status === 401 || err.status === 403) {
      this.authentificationService.logout();
      localStorage.clear();
      this.router.navigateByUrl('/login');
      this.alertService.error("You have been disconnected after an 403/401 error")
      return of(err.message);
    }
    return throwError(err);
  }
}

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
];
