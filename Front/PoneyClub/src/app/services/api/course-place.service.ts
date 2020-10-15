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
  getTeacherCoursePlacesUrl: string = `${this.BASE_URL}/teacher-course-places`;
  mapHorseToCourseUrl: string = `${this.BASE_URL}/addhorse`;
  unsubscribeCourseUrl: string = `${this.BASE_URL}/user-planning/unsubscribe`;



  constructor(private http: HttpClient) { }

  public getUserPlanning(mailOrNumber: string): Observable<ICoursePlace[]> {
    return this.http.get<ICoursePlace[]>(this.getUserPlanningUrl + '/' + mailOrNumber);
  }

  public getTeacherCoursePlaces(idTeacher: number, idCourse: number): Observable<ICoursePlace[]> {
   return this.http.get<ICoursePlace[]>(this.getTeacherCoursePlacesUrl + '/' + idTeacher + '/' + idCourse);
  }

  public mapHorseToCourse(coursePlace: ICoursePlace): Observable<any> {
    return this.http.post<any>(this.mapHorseToCourseUrl + '/' + coursePlace.horse.name + '/' +
     coursePlace.course.teacher.id + '/' + coursePlace.id, null);
  }

  public unsubscribeCourse(idCourse: number): Observable<any> {
    return this.http.delete(this.unsubscribeCourseUrl + '/' + idCourse);
  }

}
