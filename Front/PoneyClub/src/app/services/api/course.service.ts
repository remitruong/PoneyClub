import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ICourse } from 'src/app/_classes/icourse';
import { Observable } from 'rxjs';
import { User } from 'src/app/_classes';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  BASE_URL: string = 'http://localhost:8081';
  getCoursesUrl: string = `${this.BASE_URL}/course/get-courses`;
  addCourseUrl: string = `${this.BASE_URL}/course/plan`;
  registerToCourseUrl: string = `${this.BASE_URL}/course?/register?`;


  constructor(private http: HttpClient) { }

  public getCourses(): Observable<ICourse[]> {
    return this.http.get<ICourse[]>(this.getCoursesUrl);
  }

  public addCourse(course: ICourse, idTeacher: number): Observable<ICourse> {
    return this.http.post<ICourse>(this.addCourseUrl + '/' + idTeacher, course);
  }

  public registerToCourse(user: User, idCourse: number): Observable<any> {
    console.log(this.registerToCourseUrl);
     return this.http.post<any>( 'http://localhost:8081/course/register/' + idCourse, user);
  }
}
