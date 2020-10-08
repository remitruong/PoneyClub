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

  newUser: User = {
    id: 0,
    firstName: '',
    lastName:  '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: '',
    role:'',
    statut:''
  };
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
        console.log(data);
        this.users = data;
        this.alertService.success('All user refreshed');
        setTimeout(() => {
          this.alertService.clear();
        }, 1500);
      },
      error => {
        this.localError = error;
        this.alertService = this.localError.error.response;
      }
    );
  }

  updateUser(user: User) {
    this.userService.updateUser(user.id, user).subscribe(
      data => {
        this.alertService.success('Update user successful');
        setTimeout(() => {
          this.alertService.clear();
        }, 3000);
      },
      error => {
        console.log("error occured while update user" + error);
        // this.alertService.error(this.localError.error.response);
      }
    )
  }

  createTeacher(user: User) {
    this.userService.createTeacher(user, this.authenticationService.currentUserValue.email).subscribe(
      data => {
        this.selectedUser = data;
        this.users.push(this.selectedUser);
        this.alertService.success('Teacher created successful');
        setTimeout(() => {
          this.alertService.clear();
        }, 3000);
      },
      error => {
        console.log("error occured while adding user");
        // this.alertService.error(this.localError.error.response);
      }

    )
  }

  userToAdmin(user: User) {
    this.userService.changeToAdmin(user.id, this.authenticationService.currentUserValue.email).subscribe(
      data => {
        this.selectedUser.statut='Admin';
        console.log("User " + this.selectedUser.firstName + " " + this.selectedUser.lastName + " is now an administrator !");
        this.alertService.success("User " + this.selectedUser.firstName + " " + this.selectedUser.lastName + " is now an administrator !");
        setTimeout(() => {
          this.alertService.clear();
        }, 3000);
      },
      error => {
        console.log("Error while setting selected user to admin");
        // this.alertService.error(this.localError.error.response);
      }
    )
  }

  selectUser(user: User) {
   this.selectedUser = user;
  }

  addTeacher() {
    this.newUser.email='';
    this.newUser.firstName='';
    this.newUser.lastName='';
    this.newUser.mobile='';
    this.newUser.password='';
    this.newUser.role='';
    this.newUser.statut='';
    this.selectedUser=this.newUser;
  }

}
