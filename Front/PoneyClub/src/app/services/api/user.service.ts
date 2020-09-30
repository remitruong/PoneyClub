import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/classes/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private BASE_URL = 'http://localhost:8081';
  private signupUrl = `${this.BASE_URL}/user/create-rider`;

  constructor(private http: HttpClient) { }

  public signup(user: User): Observable<any> {
    return this.http.post(this.signupUrl, user);
  }


}
