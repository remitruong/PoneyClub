import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from 'src/app/_classes/user';
import { TokenStorageService } from 'src/app/_auth/token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private BASE_URL = 'http://localhost:8081';
  private signupUrl = `${this.BASE_URL}/user/create-rider`;
  private updateUserUrl = `${this.BASE_URL}/user/update-user`;
  private changeToAdminUrl = `${this.BASE_URL}/user/convert-to-admin`;
  private getUsersUrl = `${this.BASE_URL}/user/get-users`;
  private getUserUrl = `${this.BASE_URL}/user/get-user`;
  private createTeachertUrl = `${this.BASE_URL}/user/create-teacher`;
  private createAdminUrl = `${this.BASE_URL}/user/create-admin`;
  private getTeachersUrl = `${this.BASE_URL}/user/get-teachers`;;
  private getAdminsUrl = `${this.BASE_URL}/user/get-admins`;
  private forgotPasswordUrl = `${this.BASE_URL}/user/forgot-password`;
  private setNewPasswordUrl = `${this.BASE_URL}/user/reset-password`;

  constructor(private http: HttpClient, private tokenService: TokenStorageService) { }

  public signup(user: User): Observable<any> {
    return this.http.post(this.signupUrl, user);
  }

  public updateUserInformation(idUser: number, user: User): Observable<any> {
    let headers = new HttpHeaders();
    headers.set('Content-Type', 'application/json; charset=utf-8');
    return this.http.post<any>(this.updateUserUrl + '/' + idUser, user,  {headers: headers, observe: 'response'as 'body'})
    .pipe(map(
      (resp: HttpResponse<any>) => {
        this.tokenService.saveToken(resp.headers.get('Authorization'));
      }
    ))
  }

  public updateUser(idUser: number, user: User): Observable<any> {
    return this.http.post<any>(this.updateUserUrl + '/' + idUser, user);
  }

  public changeToAdmin(idUser: number, adminMail:string): Observable<any> {
    return this.http.post(this.changeToAdminUrl + '/' + idUser + '/' + adminMail, null);
  }

  public createTeacher(user: User, adminMail:string): Observable<any> {
    return this.http.post(this.createTeachertUrl + '/' + adminMail, user);
  }

  public createAdmin(user: User): Observable<any> {
    return this.http.post(this.createAdminUrl, user);
  }

  public getUsers(adminMail: string): Observable<User[]> {
    return this.http.get<User[]>(this.getUsersUrl + '/' + adminMail);
  }

  public getUser(mailOrNumber:string, adminMail:string): Observable<User> {
    return this.http.get<User>(this.getUserUrl + '/' + mailOrNumber + '/' + adminMail);
  }

  public getTeachers(): Observable<User[]> {
    return this.http.get<User[]>(this.getTeachersUrl);
  }

  public getAdmins(): Observable<User[]> {
    return this.http.get<User[]>(this.getAdminsUrl);
  }

  public forgotPassword(email:string): Observable<any> {
    return this.http.post(this.forgotPasswordUrl + '/' + email, null);
  }

  public setNewPassword(token: string, password: string): Observable<any> {
    return this.http.post(this.setNewPasswordUrl + '?token=' + token, password);
  }

}
