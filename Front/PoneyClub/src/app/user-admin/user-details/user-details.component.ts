import { Component, OnInit, Input, Output } from '@angular/core';
import { User } from 'src/app/_classes';
import { EventEmitter } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {
  submitted = false;
  userForm: FormGroup;
  mobileNumberPattern = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";
  emailPattern ="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])";

  @Input() userSelected:User;
  @Output() userUpdated: EventEmitter<User> = new EventEmitter<User>();
  @Output() userDeleted: EventEmitter<User> = new EventEmitter<User>();
  @Output() userAdded: EventEmitter<User> = new EventEmitter<User>();
  @Output() userToAdmin: EventEmitter<User> = new EventEmitter<User>();

  constructor(private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      mobile: ['', Validators.required],
      licenceNum: ['']
    });
  }

  get f() { return this.userForm.controls; }

  updateUser() {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }
    this.userUpdated.emit(this.userSelected);
  }

  deleteUser() {
    this.userDeleted.emit(this.userSelected);
  }

  addUser() {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }
    this.userAdded.emit(this.userSelected);
  }

  changetoAdmin() {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }
    this.userToAdmin.emit(this.userSelected);
  }

}
