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
import {AlertService} from "../services/alert.service";
import { ThrowStmt } from '@angular/compiler';

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
  private selectedCourse: ICourse;
  public courses: ICourse[] = [];
  public currentUser: User = null;
  public bCourseAdd = false;
  submitted = false;
  public startDateTime: string = null;
  public endDateTime: string = null;
  public courseForm: FormGroup;
  public coursePlaces : ICoursePlace[] = [];
  public selectedCoursePlaces: ICoursePlace[] = [];
  private localError : IError;

  constructor(private courseService: CourseService, private coursePlaceService: CoursePlaceService ,private authenticationService: AuthenticationService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  public ngOnInit(): void {
    this.currentUser = this.authenticationService.currentUserValue;
    this.submitted = false;
    this.courseForm = this.formBuilder.group({
      title: ['', Validators.required],
      startDateTime: ['', Validators.required],
      endDateTime: ['', Validators.required],
      level: ['', [Validators.required, Validators.min(1),, Validators.max(8)]],
      maxStudent: ['', [Validators.required, Validators.min(1),, Validators.max(10)] ],
    });
    this.getCourse();
    this.getUserPlanning();
  }

  getUserPlanning() {
    this.coursePlaceService.getUserPlanning(this.authenticationService.currentUserValue.email).subscribe(
      data => {
        this.coursePlaces = data;
      },
      error => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      })
  }

  getCourse(){
    this.courseService.getCourses().subscribe(
      data => {
        this.courses = data;
        for (let course of this.courses) {
          this.courseService.getAvailablePlaces(course.id).subscribe(
            data => {
              course.availablePlaces = data;
              this.alertService.success('Course refresh successfull');
              this.alertService.clearAfter(1500);
            },
            error => {
             this.localError = error;
             this.alertService.error(this.localError.error);
            }
          )
        }
      },
      error => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      },
    );
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
        this.course = data;
        this.course.availablePlaces = this.course.maxStudent;
        this.courses.push(data);
        this.alertService.success('Course well added');
        this.alertService.clearAfter(1500);
      },
      error => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      },
    );
    this.bCourseAdd = false;
  }

  subscribe(course: ICourse) {
    this.courseService.registerToCourse(this.currentUser, course.id).subscribe (
      (data) => {
        this.coursePlaces.push(data);
        this.alertService.success('Subscription success');
        this.alertService.clearAfter(1500);
      },
      (err) => {
        this.localError = err;
        this.alertService.error(this.localError.error);
      },
    );
  }

  unsubscribe(coursePlace: ICoursePlace){
    this.coursePlaceService.unsubscribeCourse(coursePlace.id).subscribe (
      (data) => {
        let indexCoursePlace = this.coursePlaces.indexOf(coursePlace);
        this.coursePlaces.splice(indexCoursePlace, 1);
        this.alertService.success('Unsubscribe success');
        this.alertService.clearAfter(1500);
      },
      (err) => {
        this.localError = err;
        this.alertService.error(this.localError.error);
      },
    );
  }

  selectCourse(course: ICourse) {
    this.selectedCourse = course;
    this.coursePlaceService.getTeacherCoursePlaces(this.selectedCourse.teacher.id, this.selectedCourse.id).subscribe(
      data => {
        this.selectedCoursePlaces = data;
      }, 
      error => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      }
    )
  }

  mapHorseToCourse(coursePlace: ICoursePlace) {
    this.coursePlaceService.mapHorseToCourse(coursePlace).subscribe(
      data => {
        this.alertService.success("Horse well mapped");
      },
      error => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      }
    )
  }

  get f() { return this.courseForm.controls; }

}
