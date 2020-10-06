import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/api/user.service';
import { User } from '../_classes';
import { IError } from '../_classes/ierror';
import { AlertService } from '../services/alert.service';
import { AuthenticationService } from '../services/authentification.service';

@Component({
  selector: 'app-user-admin',
  templateUrl: './user-admin.component.html',
  styleUrls: ['./user-admin.component.css']
})
export class UserAdminComponent implements OnInit {

  users: User[] = [];
  localError : IError;
  selectedUser: User;

  constructor(private userService : UserService, private alertService : AlertService, private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.getAllUsers();
  }


  getAllUsers() {
    this.userService.getUsers(this.authenticationService.currentUserValue.email).subscribe(
      data => {
        this.users = data;
      },
      error => {
        this.localError = error;
        this.alertService = this.localError.error.response;
      }
    );
  }

  updateUser(user: User) {
    this.userService.updateUser(user).subscribe(
      data => {
        console.log("user update successfull");
      },
      error => {
        console.log("error occured while update user");
      }
    )
  }

  selectUser(user: User) {
    this.selectedUser = user;
  }

}
