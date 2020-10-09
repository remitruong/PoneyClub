import { Component, OnInit } from '@angular/core';
import { ICourse } from '../_classes/icourse';
import { CourseService } from '../services/api/course.service';
import { AuthenticationService } from '../services/authentification.service';
import { DateTimePipe } from '../share/pipe/date-time.pipe';
import { User } from '../_classes';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  course: ICourse = {
    id: 0,
    title: '',
    startDateTime: '',
    endDateTime: '',
    levelStudying: '',
    maxStudent: 0
  };
  courses: ICourse[] = [];
  currentUser: User = null;
  bCourseAdd = false;
  startDateTime: string = null;
  endDateTime: string = null;

  constructor(private courseService: CourseService, private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.currentUser = this.authenticationService.currentUserValue;

    this.courseService.getCourses().subscribe(
      data => {
        this.courses = data;
      },
      error => {

      }
    )
  }

  addCourse() {
    this.bCourseAdd = true;
  }

  createCourse() {
    this.course.startDateTime = new DateTimePipe().transform(this.startDateTime);
    this.course.endDateTime = new DateTimePipe().transform(this.endDateTime);

    this.courseService.addCourse(this.course, this.currentUser.id).subscribe(
      data => {
        console.log(data);
        this.courses.push(data);
        console.log("course well added");
      },
      error => {
        console.log("error while adding course");
      }
    )
    this.course = null;
    this.bCourseAdd = false;
  }

}
