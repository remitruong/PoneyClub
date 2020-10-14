import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IHorse } from 'src/app/_classes/ihorse';

@Injectable({
  providedIn: 'root'
})
export class HorseService {

  BASE_URL: string = 'http://localhost:8081';
  createHorseUrl: string = `${this.BASE_URL}/horse/create-horse`;
  getHorsesUrl: string = `${this.BASE_URL}/horse/list-horse`;
  updateHorseUrl: string = `${this.BASE_URL}/horse/update-horse`;
  deleteHorseUrl: string = `${this.BASE_URL}/horse/delete-horse`;

  constructor(private http: HttpClient) { }

  public createHorse(horseName: string, idAdmin: number): Observable<IHorse> {
    return this.http.post<IHorse>(this.createHorseUrl + '/' + horseName + '/' + idAdmin, null);
  }

  public getHorses(): Observable<IHorse[]> {
    return this.http.get<IHorse[]>(this.getHorsesUrl);
  }

  public updateHorse(horse: IHorse, idAdmin: number): Observable<any> {
    return this.http.post(this.updateHorseUrl + '/' + horse.id + '/' + horse.name + '/' + idAdmin, null);
  }

    public deleteHorse(horse: IHorse, idAdmin: number): Observable<any> {
    return this.http.delete(this.deleteHorseUrl + '/' + horse.id + '/' + idAdmin);
  }

}
