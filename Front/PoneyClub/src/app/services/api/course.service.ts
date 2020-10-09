import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ICourse } from 'src/app/_classes/icourse';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  BASE_URL: string = 'http://localhost:8081';
  getCoursesUrl: string = `${this.BASE_URL}/course/get-courses`;
  addCourseUrl: string = `${this.BASE_URL}/course/plan`;

  constructor(private http: HttpClient) { }

  public getCourses():Observable<ICourse[]> {
    return this.http.get<ICourse[]>(this.getCoursesUrl);
  }

    public addCourse(course: ICourse, idTeacher: number):Observable<ICourse> {
    return this.http.post<ICourse>(this.addCourseUrl + '/' + idTeacher, course);
  }


}
