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
  userForm: FormGroup;
  submitted = false;
  mobileNumberPattern = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";
  emailPattern ="([a-zA-Z0-9_.]{1,})((@[a-zA-Z]{2,})[\\\.]([a-zA-Z]{2}|[a-zA-Z]{3}))";

  @Input() userSelected:User;
  @Output() userUpdated: EventEmitter<User> = new EventEmitter<User>();
  @Output() userDeleted: EventEmitter<User> = new EventEmitter<User>();
  @Output() userAdded: EventEmitter<User> = new EventEmitter<User>();

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.submitted = false;
    this.userForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      mobile: ['', Validators.required],
      licenceNum: ['']
    });
  }

  get f() { return this.userForm.controls; }

  updateAdmin() {
    this.submitted = true;
    //disable the control on password
    this.userForm.get('password').disable();
    if (this.userForm.invalid) {
      return;
    }
    this.userUpdated.emit(this.userSelected);
  }

  deleteAdmin() {
    this.userDeleted.emit(this.userSelected);
  }

  addAdmin() {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }
    this.userAdded.emit(this.userSelected);
  }

}