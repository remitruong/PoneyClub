import { Component, OnInit, Input, Output } from '@angular/core';
import { User } from 'src/app/_classes';
import { EventEmitter } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-admin-details',
  templateUrl: './admin-details.component.html',
  styleUrls: ['./admin-details.component.css']
})
export class AdminDetailsComponent implements OnInit {
  superAdminForm: FormGroup;
  submitted = false;
  mobileNumberPattern = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";
  emailPattern ="([a-zA-Z0-9_.]{1,})((@[a-zA-Z]{2,})[\\\.]([a-zA-Z]{2}|[a-zA-Z]{3}))";

  @Input() adminSelected:User;
  @Output() adminUpdated: EventEmitter<User> = new EventEmitter<User>();
  @Output() adminDeleted: EventEmitter<User> = new EventEmitter<User>();
  @Output() adminAdded: EventEmitter<User> = new EventEmitter<User>();

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.submitted = false;
    this.superAdminForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      mobile: ['', Validators.required]
    });
  }

  get f() { return this.superAdminForm.controls; }

  updateAdmin() {
    this.submitted = true;
    //disable the control on password
    this.superAdminForm.get('password').disable();
    if (this.superAdminForm.invalid) {
      return;
    }
    this.adminUpdated.emit(this.adminSelected);
  }

  deleteAdmin() {
    this.adminDeleted.emit(this.adminSelected);
  }

  addAdmin() {
    this.submitted = true;
    if (this.superAdminForm.invalid) {
      return;
    }
    this.adminAdded.emit(this.adminSelected);
  }

}
