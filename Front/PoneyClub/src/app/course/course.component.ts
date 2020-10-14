import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../_classes';
import { ICourse } from '../_classes/icourse';
import { CourseService } from '../services/api/course.service';
import { AuthenticationService } from '../services/authentification.service';
import { DateTimePipe } from '../share/pipe/date-time.pipe';
import { ICoursePlace } from '../_classes/icourseplace';
import { CoursePlaceService } from '../services/api/course-place.service';
import { IError } from '../_classes/ierror';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css'],
})
export class CourseComponent implements OnInit {

  public course: ICourse = {
    id: 0,
    title: '',
    startDateTime: '',
    endDateTime: '',
    levelStudying: '',
    maxStudent: 0,
    availablePlaces: 0,
    teacher: null,
  };
  public courses: ICourse[] = [];
  public currentUser: User = null;
  public bCourseAdd = false;
  submitted = false;
  public startDateTime: string = null;
  public endDateTime: string = null;
  public courseForm: FormGroup;
  public coursePlaces : ICoursePlace[] = [];

  constructor(private courseService: CourseService, private coursePlaceService: CoursePlaceService ,private authenticationService: AuthenticationService, private formBuilder: FormBuilder) { }

  public ngOnInit(): void {
    this.currentUser = this.authenticationService.currentUserValue;
    this.submitted = false;
    this.courseForm = this.formBuilder.group({
      title: ['', Validators.required],
      startDateTime: ['', Validators.required],
      endDateTime: ['', Validators.required],
      level: ['', Validators.required],
      maxStudent: ['', Validators.required],
    });


    this.courseService.getCourses().subscribe(

      (data) => {
        this.courses = data;
         for (let course of this.courses) {
      console.log("test"+ course.id);
      this.courseService.getAvailablePlaces(course.id).subscribe(
        data => {
          console.log(data);
          course.availablePlaces = data;
        },
        error => {
          console.log(error);
        }
      )
    }
      },
      (error) => {

      },
    );



    this.coursePlaceService.getUserPlanning(this.authenticationService.currentUserValue.email).subscribe(
      data => {
        this.coursePlaces = data;
      },
      error => {
        console.log(error);
      }
    )
  }

  addCourse() {
    this.bCourseAdd = true;
    this.course.title = '';
    this.course.startDateTime = '';
    this.course.endDateTime = '';
    this.course.levelStudying = '';
    this.course.maxStudent = null;
    this.course.teacher = null;
    this.startDateTime = '';
    this.endDateTime = '';
  }

  createCourse() {
    this.course.startDateTime = new DateTimePipe().transform(this.startDateTime);
    this.course.endDateTime = new DateTimePipe().transform(this.endDateTime);

    this.submitted = true;

    if (this.courseForm.invalid) {
      return;
    }
    this.courseService.addCourse(this.course, this.currentUser.id).subscribe(
      data => {
        console.log(data);
        this.courses.push(data);
        console.log('course well added');
      },
      error => {
        let ierror: IError = error;
        console.log('error while adding course' + ierror.error);
      },
    );
    this.bCourseAdd = false;
  }

  subscribe(course: ICourse) {
    this.courseService.registerToCourse(this.currentUser, course.id).subscribe (
      (data) => {
        console.log(data);
        this.coursePlaces.push(data);
        console.log('Subscribe succes ! ');
      },
      (err) => {
        console.log('error while subscribing to course');
      },
    );
  }

  unsubscribe(coursePlace: ICoursePlace){
    this.coursePlaceService.unsubscribeCourse(coursePlace.id).subscribe (
      (data) => {
        let indexCoursePlace = this.coursePlaces.indexOf(coursePlace);
        this.coursePlaces.splice(indexCoursePlace, 1);
        console.log('Unsubscribe succes ! ');
      },
      (err) => {
        console.log('error while unsubscribing to course');
      },
    );
  }

  get f() { return this.courseForm.controls; }

}
