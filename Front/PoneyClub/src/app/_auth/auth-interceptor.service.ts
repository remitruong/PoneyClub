import { Injectable } from '@angular/core';
import {HTTP_INTERCEPTORS, HttpErrorResponse} from '@angular/common/http';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import {catchError} from "rxjs/operators";
import {Observable, of, throwError} from "rxjs";
import {Router} from "@angular/router";


const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor{
  private router: Router;

    constructor(private token: TokenStorageService) { }

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
      this.router.navigateByUrl('/login');
      return of(err.message);
    }
    return throwError(err);
  }
}

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
];
