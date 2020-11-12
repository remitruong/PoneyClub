import { Component, EventEmitter, OnInit, Input, Output } from '@angular/core';
import { Icourse } from 'src/app/_classes/icourse';
import { StringToDatetimePipe } from 'src/app/share/pipe/string-to-datetime.pipe';
import { DateTimeTostringPipe } from 'src/app/share/pipe/date-time-tostring.pipe';

@Component({
  selector: 'app-update-course',
  templateUrl: './update-course.component.html',
  styleUrls: ['./update-course.component.css']
})
export class UpdateCourseComponent implements OnInit {

  @Input() selectedCourse: Icourse;
  @Output() updateCourse : EventEmitter<Icourse> = new EventEmitter<Icourse>();
  endDate: string;
  startDate: string;

  constructor() { }

  ngOnInit(): void {
    this.startDate = new StringToDatetimePipe().transform(this.selectedCourse.startDateTime);
    this.endDate = new StringToDatetimePipe().transform(this.selectedCourse.endDateTime);
  }

  sendUpdatedCourse() {
    this.selectedCourse.startDateTime = new DateTimeTostringPipe().transform(this.startDate);
    this.selectedCourse.endDateTime = new DateTimeTostringPipe().transform(this.endDate);
    this.updateCourse.emit(this.selectedCourse);
  }

}
