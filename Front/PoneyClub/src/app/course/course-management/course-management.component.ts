import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ICoursePlace} from 'src/app/_classes/icourseplace';
import {HorseService} from 'src/app/services/api/horse.service';
import {IHorse} from 'src/app/_classes/ihorse';
import {AlertService} from 'src/app/services/alert.service';
import {IError} from 'src/app/_classes/ierror';

@Component({
  selector: 'app-course-management',
  templateUrl: './course-management.component.html',
  styleUrls: ['./course-management.component.css']
})
export class CourseManagementComponent implements OnInit {

  @Input() selectedCoursePlaces: ICoursePlace;
  @Output() addHorseToCoursePlace : EventEmitter<ICoursePlace> = new EventEmitter<ICoursePlace>();
  private localError: IError;
  horse: IHorse = {
    id: 0,
    name: '',
  };
  horses: IHorse[] = [];
  coursePlace : ICoursePlace;

  constructor(private horseService: HorseService, private alertService: AlertService) { }

  ngOnInit(): void {
    this.horseService.getHorses().subscribe(
      data => {
        this.horses = data;
      },
      error => {
        this.alertService.error(this.localError.error);
      }
    )
  }

  selectHorse(horse: IHorse) {
    this.horse = horse;
  }

  validate(coursePlace: ICoursePlace) {
    this.coursePlace = coursePlace;
    this.coursePlace.horse = this.horse;
    console.log(this.coursePlace);
    this.addHorseToCoursePlace.emit(this.coursePlace);
  }

}
