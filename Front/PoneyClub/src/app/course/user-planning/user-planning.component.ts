import { Component, OnInit, Input, Output } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentification.service';
import { ICourse } from 'src/app/_classes/icourse';
import { EventEmitter } from '@angular/core';
import { ICoursePlace } from 'src/app/_classes/icourseplace';

@Component({
  selector: 'app-user-planning',
  templateUrl: './user-planning.component.html',
  styleUrls: ['./user-planning.component.css']
})
export class UserPlanningComponent implements OnInit {

  @Input() coursePlaces: ICoursePlace[];
  @Output() courseUnsubscribe: EventEmitter<number> = new EventEmitter<number>();

  constructor(private authenticationService : AuthenticationService) { }

  ngOnInit(): void {
  }

  unsubscribe(coursePlaceId: number) {
    this.courseUnsubscribe.emit(coursePlaceId);
  }

}
