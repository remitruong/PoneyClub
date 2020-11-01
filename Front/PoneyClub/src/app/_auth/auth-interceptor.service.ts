import { Injectable } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';


const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor{

    constructor(private token: TokenStorageService) { }
 
    intercept(req: HttpRequest<any>, next: HttpHandler) {
        let authReq = req;
        const token = this.token.getToken();
        if (token != null) {
            authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, token) });
        }
        return next.handle(authReq);
    }
}
 
export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
];
