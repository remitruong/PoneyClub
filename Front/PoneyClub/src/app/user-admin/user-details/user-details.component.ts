import { Component, Input, OnInit, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { User } from 'src/app/_classes';
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css'],
})
export class UserDetailsComponent implements OnInit {
  public userForm: FormGroup;
  public submitted = false;
  public mobileNumberPattern = '^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$';
  public emailPattern ='([a-zA-Z0-9_.]{1,})((@[a-zA-Z]{2,})[\\\.]([a-zA-Z]{2}|[a-zA-Z]{3}))';

  @Input() public userSelected: User;
  @Output() public userUpdated: EventEmitter<User> = new EventEmitter<User>();
  @Output() public userDeleted: EventEmitter<User> = new EventEmitter<User>();
  @Output() public userAdded: EventEmitter<User> = new EventEmitter<User>();
  @Output() public userToAdmin: EventEmitter<User> = new EventEmitter<User>();

  constructor(private formBuilder: FormBuilder, private alertService: AlertService) {}

  public ngOnInit(): void {
    this.submitted = false;
    this.userForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      mobile: ['', Validators.required],
      licenceNum: [''],
    });
  }

  get f() { return this.userForm.controls; }

  updateUser() {
    this.submitted = true;
    // disable the control on password
    this.userForm.get('password').disable();
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
    this.userToAdmin.emit(this.userSelected);
  }

}
