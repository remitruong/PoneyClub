import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ICourse } from 'src/app/_classes/icourse';
import { Observable } from 'rxjs';
import { User } from 'src/app/_classes';
import { ICoursePlace } from 'src/app/_classes/icourseplace';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  BASE_URL: string = 'http://localhost:8081/course';
  getCoursesUrl: string = `${this.BASE_URL}/get-courses`;
  addCourseUrl: string = `${this.BASE_URL}/plan`;
  registerToCourseUrl: string = `${this.BASE_URL}/register`;
  availablePlacesUrl: string =  `${this.BASE_URL}/available-places`;


  constructor(private http: HttpClient) { }

  public getCourses(): Observable<ICourse[]> {
    return this.http.get<ICourse[]>(this.getCoursesUrl);
  }

  public addCourse(course: ICourse, idTeacher: number): Observable<ICourse> {
    return this.http.post<ICourse>(this.addCourseUrl + '/' + idTeacher, course);
  }

  public registerToCourse(user: User, idCourse: number): Observable<ICoursePlace> {
    console.log(this.registerToCourseUrl);
     return this.http.post<ICoursePlace>( this.registerToCourseUrl + '/' + idCourse, user);
  }

  public getAvailablePlaces(idCourse: number): Observable<any> {
    return this.http.get(this.availablePlacesUrl + '/' + idCourse);
  }

}
