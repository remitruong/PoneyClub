import { Component, EventEmitter, OnInit, Input, Output } from '@angular/core';
import { Icourse } from 'src/app/_classes/icourse';
import { StringToDatetimePipe } from 'src/app/share/pipe/string-to-datetime.pipe';
import { DateTimeTostringPipe } from 'src/app/share/pipe/date-time-tostring.pipe';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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
  updateForm: FormGroup;
  submitted= false;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.submitted = false;
    this.startDate = new StringToDatetimePipe().transform(this.selectedCourse.startDateTime);
    this.endDate = new StringToDatetimePipe().transform(this.selectedCourse.endDateTime);
    this.updateForm = this.formBuilder.group({
      titleUpdate: ['', Validators.required],
      startDateTimeUpdate: ['', Validators.required],
      endDateTimeUpdate: ['', Validators.required],
      levelUpdate: ['', [Validators.required, Validators.min(1), , Validators.max(8)]],
      maxStudentUpdate: ['', [Validators.required, Validators.min(1), , Validators.max(10)]],
    });
  }

  get f() { return this.updateForm.controls; }


  sendUpdatedCourse() {
    this.submitted = true;

    if (this.updateForm.invalid) {
      return;
    }

    this.selectedCourse.startDateTime = new DateTimeTostringPipe().transform(this.startDate);
    this.selectedCourse.endDateTime = new DateTimeTostringPipe().transform(this.endDate);
    this.updateCourse.emit(this.selectedCourse);
  }

}
