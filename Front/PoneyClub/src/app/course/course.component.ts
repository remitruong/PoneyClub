import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../_classes';
import { Icourse } from '../_classes/icourse';
import { ICoursePlace } from '../_classes/icourseplace';
import { IError } from '../_classes/ierror';
import { AlertService } from '../services/alert.service';
import { CoursePlaceService } from '../services/api/course-place.service';
import { CourseService } from '../services/api/course.service';
import { UserService } from '../services/api/user.service';
import { AuthenticationService } from '../services/authentification.service';
import { DateTimePipe } from '../share/pipe/date-time.pipe';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css'],
})
export class CourseComponent implements OnInit {

  public course: Icourse = {
    id: 0,
    title: '',
    startDateTime: '',
    endDateTime: '',
    levelStudying: '',
    maxStudent: 0,
    availablePlaces: 0,
    teacher: null,
    showManageButton: false,
  };
  private newCourse: Icourse = {
    id: 0,
    title: '',
    startDateTime: '',
    endDateTime: '',
    levelStudying: '',
    maxStudent: 0,
    availablePlaces: 0,
    teacher: null,
    showManageButton: false,
  };
  private allTeachers: User = {
    id: 0,
    firstName: 'All',
    lastName: 'teachers',
    email: '',
    password: '',
    mobile: '',
    licenceNum: '',
    role: '',
    statut: '',
  };
  searchTeacher;
  private selectedCourse: Icourse;
  public courses: Icourse[] = [];
  public filteredCourse: Icourse[] = [];
  public currentUser: User = null;
  public bCourseAdd = false;
  public bRecurrentCourseAdd = false;
  public createCourseButton = false;
  public submitted = false;
  public startDateTime: string = null;
  public endDateTime: string = null;
  public courseForm: FormGroup;
  public coursePlaces: ICoursePlace[] = [];
  public selectedCoursePlaces: ICoursePlace[] = [];
  private localError: IError;
  public teachers: User[] = [
    this.allTeachers,
  ];
  private selectedTeacher: User = null;
  private selectedLevel = null;
  public levels = [];
  public level = null;
  public recurrence : string = null;

  constructor(private courseService: CourseService, private coursePlaceService: CoursePlaceService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder, private alertService: AlertService,
    private userService: UserService) { }

  public ngOnInit(): void {
    this.currentUser = this.authenticationService.currentUserValue;
    this.submitted = false;
    this.courseForm = this.formBuilder.group({
      title: ['', Validators.required],
      startDateTime: ['', Validators.required],
      endDateTime: ['', Validators.required],
      level: ['', [Validators.required, Validators.min(1), , Validators.max(8)]],
      maxStudent: ['', [Validators.required, Validators.min(1), , Validators.max(10)]],
    });
    this.getCourses();
    this.getUserPlanning();
    this.getTeachers();
  }

  getUserPlanning() {
    this.coursePlaceService.getUserPlanning(this.authenticationService.currentUserValue.email).subscribe(
      (data) => {
        this.coursePlaces = data;
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      });
  }

  getCourses() {
    this.courseService.getCourses().subscribe(
      (data) => {
        this.courses = data;
        this.filteredCourse = this.courses;
        for (const course of this.courses) {

          var courseDate = new Date(course.startDateTime);
          var courseDateBefore = new Date(course.startDateTime);
          courseDateBefore.setHours(courseDate.getHours() - 24)
          var now = new Date();

          if(now<courseDateBefore){
            course.showManageButton = true;
          }

          this.courseService.getAvailablePlaces(course.id).subscribe(
            data => {
              course.availablePlaces = data;
              this.alertService.success('Course refresh successfull');
              this.alertService.clearAfter(1500);
            },
            (error) => {
              this.localError = error;
              this.alertService.error(this.localError.error);
            }
          );
        }
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      },
    );
  }

  getTeachers() {
    const teachersTemp = this.teachers;
    this.teachers = [];
    const idList = [];
    this.teachers.push(this.allTeachers)
    this.courses.forEach((x) => {
      if (!idList.includes(x.teacher.id)) {
        idList.push(x.teacher.id);
        this.teachers.push(x.teacher);
      }
    })
  }

  getLevels() {
    this.levels = [];
    this.courses.forEach((x) =>
      this.levels.push(x.levelStudying),
    );
    this.levels.push('All');
    this.levels = Array.from(new Set(this.levels));
  }

  addCourse() {
    this.createCourseButton = true;
    this.bRecurrentCourseAdd = false;
    this.bCourseAdd = true;
    this.newCourse.title = '';
    this.newCourse.startDateTime = '';
    this.newCourse.endDateTime = '';
    this.newCourse.levelStudying = '';
    this.newCourse.maxStudent = null;
    this.newCourse.teacher = null;
    this.startDateTime = '';
    this.endDateTime = '';
  }
  addRecurrentCourse() {
    this.createCourseButton = false;
    this.bCourseAdd = true;
    this.bRecurrentCourseAdd = true;
    this.newCourse.title = '';
    this.newCourse.startDateTime = '';
    this.newCourse.endDateTime = '';
    this.newCourse.levelStudying = '';
    this.newCourse.maxStudent = null;
    this.newCourse.teacher = null;
    this.startDateTime = '';
    this.endDateTime = '';
  }

  createCourse() {
    this.newCourse.startDateTime = new DateTimePipe().transform(this.startDateTime);
    this.newCourse.endDateTime = new DateTimePipe().transform(this.endDateTime);
    this.newCourse.teacher = this.authenticationService.currentUserValue;

    this.submitted = true;

    if (this.courseForm.invalid) {
      return;
    }

    this.courseService.addCourse(this.newCourse, this.currentUser.id).subscribe(
      (data) => {
        this.course = data;
        this.course.availablePlaces = this.course.maxStudent;
        this.courses.push(data);
        this.alertService.success('Course well added');
        this.alertService.clearAfter(1500);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      },
    );
    this.bCourseAdd = false;
    this.submitted = false;
  }

  createRecurrentCourse(){
    this.newCourse.startDateTime = new DateTimePipe().transform(this.startDateTime);
    this.newCourse.endDateTime = new DateTimePipe().transform(this.endDateTime);
    this.newCourse.teacher = this.authenticationService.currentUserValue;

    this.submitted = true;

    if (this.courseForm.invalid) {
      return;
    }

    this.courseService.addRecurrentCourse(this.newCourse, this.recurrence).subscribe(
      (data) => {
        const recurrentCourses: Icourse[] = data;
        for (let recurrentCourse of recurrentCourses) {
          this.courses.push(recurrentCourse);
        }
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      }
    )
  }

  subscribe(course: Icourse) {
    this.courseService.registerToCourse(this.currentUser, course.id).subscribe(
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

  unsubscribe(coursePlace: ICoursePlace) {
    this.coursePlaceService.unsubscribeCourse(coursePlace.id).subscribe(
      (data) => {
        const indexCoursePlace = this.coursePlaces.indexOf(coursePlace);
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

  selectCourse(course: Icourse) {
    this.selectedCourse = course;
    this.coursePlaceService.getTeacherCoursePlaces(this.selectedCourse.teacher.id, this.selectedCourse.id).subscribe(
      (data) => {
        if(data.length>=3){
          console.log("Il y a assez de participants");
          this.alertService.success("enough rider")
        }else{
          this.alertService.error("No enough rider")
          console.log("Il n'y a pas assez de participants");
        }
        this.selectedCoursePlaces = data;
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      },
    );
  }

  selectTeacher(teacher: User) {
    this.selectedTeacher = teacher;
    this.filter();
  }

  selectLevel(level) {
    this.selectedLevel = level;
    this.filter();
  }

  mapHorseToCourse(coursePlace: ICoursePlace) {
    this.coursePlaceService.mapHorseToCourse(coursePlace).subscribe(
      (data) => {
        this.alertService.success('Horse well mapped');
        this.alertService.clearAfter(1500);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error);
      },
    );
  }

  selectRecurrence(recurrence: string) {
    this.recurrence = recurrence;
  }

  filter() {
    this.filteredCourse = [];
    if (this.selectedLevel === null || this.selectedLevel === 'All') {
      if (this.selectedTeacher !== this.allTeachers && this.selectedTeacher !== null) {
        this.courses.forEach( (x) => {
          if ( x.teacher.id === this.selectedTeacher.id) {
          this.filteredCourse.push(x);
          }
        })
      } else {
        this.filteredCourse = this.courses;
      }
      return;
    }

    if (this.selectedTeacher === null || this.selectedTeacher === this.allTeachers) {
      if (this.selectedLevel !== 'All') {
        this.courses.forEach( (x) => {
          if (x.levelStudying === this.selectedLevel) {
            this.filteredCourse.push(x);
          }
        })
      } else {
        this.filteredCourse = this.courses;
      }
      return;
    }

    if (  (this.selectedTeacher !== null && this.selectedTeacher !== this.allTeachers) &&
          (this.selectedLevel !== null  && this.selectedLevel !== 'All') ) {
      this.courses.forEach( (x) => {
        if ( x.teacher.id ===  this.selectedTeacher.id && this.selectedLevel === x.levelStudying) {
          this.filteredCourse.push(x);
        }
      })
    }
  }

  get f() { return this.courseForm.controls; }

}
