import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ICoursePlace } from 'src/app/_classes/icourseplace';

@Injectable({
  providedIn: 'root'
})
export class CoursePlaceService {

  BASE_URL : string = 'http://localhost:8081/place';
  getCoursesPlacesUrl: string = `${this.BASE_URL}`;
  getUserPlanningUrl: string = `${this.BASE_URL}/user-planning`;


  constructor(private http: HttpClient) { }

  public getUserPlanning(mailOrNumber: string): Observable<ICoursePlace[]> {
    return this.http.get<ICoursePlace[]>(this.getUserPlanningUrl + '/' + mailOrNumber);
  }

}
