import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {User} from "../_classes";
import {Router} from "@angular/router";
import {UserService} from "./api/user.service";

let users = JSON.parse(localStorage.getItem('users')) || [];

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private BASE_URL = 'http://localhost:8081';
  private connectUrl = `${this.BASE_URL}/user/connect`;
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;


  constructor(private http: HttpClient, private router: Router, private userService: UserService) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  login(user) {
    return this.http.post<any>(this.connectUrl, user)
      .pipe(map(
      user => {
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
