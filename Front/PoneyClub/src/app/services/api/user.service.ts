import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/_classes/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private BASE_URL = 'http://localhost:8081';
  private signupUrl = `${this.BASE_URL}/user/create-rider`;
  private connectUrl = `${this.BASE_URL}/user/connect`;
  private updateUsertUrl = `${this.BASE_URL}/user/update-user`;
  private changeToAdminUrl = `${this.BASE_URL}/user/convert-to-admin`;
  private getUsersUrl = `${this.BASE_URL}/user/get-users`;  
  private getUserUrl = `${this.BASE_URL}/user/get-user`;
  private createTeachertUrl = `${this.BASE_URL}/user/create-teacher`;


  constructor(private http: HttpClient) { }

  public signup(user: User): Observable<any> {
    return this.http.post(this.signupUrl, user);
  }

  public updateUser(idUser: number, user:User): Observable<any> {
    return this.http.post(this.updateUsertUrl+'/'+idUser, user);
  }

  public changeToAdmin(idUser: number, adminMail:string): Observable<any> {
    return this.http.post(this.changeToAdminUrl + '/'+ idUser + '/' + adminMail, null);
  }

  public createTeacher(user:User, adminMail:string): Observable<any> {
    return this.http.post(this.createTeachertUrl + '/' + adminMail, user);
  }

  public getUsers(adminMail:string): Observable<User[]> {
    return this.http.get<User[]>(this.getUsersUrl + '/' + adminMail);
  }

  public getUser(mailOrNumber:string, adminMail:string): Observable<User> {
    return this.http.get<User>(this.getUserUrl + '/' + mailOrNumber + '/' + adminMail);
  }



}
