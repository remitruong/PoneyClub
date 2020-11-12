import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ICoursePlace} from 'src/app/_classes/icourseplace';
import {IError} from 'src/app/_classes/ierror';
import {IHorse} from 'src/app/_classes/ihorse';
import {AlertService} from 'src/app/services/alert.service';
import {HorseService} from 'src/app/services/api/horse.service';

@Component({
  selector: 'app-course-management',
  templateUrl: './course-management.component.html',
  styleUrls: ['./course-management.component.css'],
})
export class CourseManagementComponent implements OnInit {

  @Input() public selectedCoursePlaces: ICoursePlace;
  @Output() public addHorseToCoursePlace: EventEmitter<ICoursePlace> = new EventEmitter<ICoursePlace>();
  private localError: IError;
  public horse: IHorse = {
    id: 0,
    name: '',
  };
  public horses: IHorse[] = [];
  public coursePlace: ICoursePlace;

  constructor(private horseService: HorseService, private alertService: AlertService) { }

  public ngOnInit(): void {
    this.horseService.getHorses().subscribe(
      (data) => {
        this.horses = data;
      },
      (error) => {
        this.alertService.error(this.localError.error.response);
      },
    );
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
