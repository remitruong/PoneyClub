import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {User} from "../_classes";
import {Router} from "@angular/router";
import {UserService} from "./api/user.service";
import { LoginModel } from '../_classes/loginmodel';
import { TokenStorageService } from '../_auth/token-storage.service';

let users = JSON.parse(localStorage.getItem('users')) || [];

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private BASE_URL = 'http://localhost:8081';
  private connectUrl = `${this.BASE_URL}/login`;
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;
  private user:User;

  constructor(private http: HttpClient, private router: Router, private userService: UserService, private tokenService: TokenStorageService) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

   public login(loginModel: LoginModel): Observable<any> {

    const loginForm = JSON.stringify({username: loginModel.username,
                                 password: loginModel.password});
    let headers = new HttpHeaders();
      headers.set('Content-Type', 'application/json; charset=utf-8');

    return this.http.post<any>(this.connectUrl, loginForm, {headers: headers, observe: 'response' as 'body'})
    .pipe(map(
      user => { 
        location.reload();
        this.user = user; 
        this.tokenService.saveToken(user.headers.get('Authorization'));
        localStorage.setItem('currentUser', JSON.stringify(user.body));
        this.currentUserSubject.next(user);
        return user;
      }))
  }

  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

}
