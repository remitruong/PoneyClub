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
    this.alertService.success('Update successful');
    setTimeout(() => {
      this.alertService.clear();
    }, 1000); //1000 equals the time in miliseconds
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
