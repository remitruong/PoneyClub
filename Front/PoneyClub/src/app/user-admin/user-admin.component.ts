import {Component, OnInit} from '@angular/core';
import {User} from '../_classes';
import {IError} from '../_classes/ierror';
import {AlertService} from '../services/alert.service';
import {UserService} from '../services/api/user.service';
import {AuthenticationService} from '../services/authentification.service';
import {Role} from "../_classes/role";

@Component({
  selector: 'app-user-admin',
  templateUrl: './user-admin.component.html',
  styleUrls: ['./user-admin.component.css'],
})
export class UserAdminComponent implements OnInit {

  public newUser: User = {
    id: 0,
    firstName: '',
    lastName:  '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: '',
    role: '',
    statut: '',
  };
  public users: User[] = [];
  public localError: IError;
  public selectedUser: User;
  public searchText;
  public display = false;

  constructor(private userService: UserService, private alertService: AlertService,
              private authenticationService: AuthenticationService) { }

  public ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers() {
    this.userService.getUsers(this.authenticationService.currentUserValue.email).subscribe(
      (data) => {
        this.users = data;
        this.alertService.success('All user refreshed');
        this.alertService.clearAfter(5000);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response);
      },
    );
  }

  updateUser(user: User) {
    this.userService.updateUser(user.id, user).subscribe(
      (data) => {
        this.alertService.success('Update user successful');
        this.alertService.clearAfter(5000);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response);
      },
    );
  }

  createUser(user: User) {
    if (user.statut === 'Teacher') {
      this.userService.createTeacher(user, this.authenticationService.currentUserValue.email).subscribe(
        (data) => {
          this.display = false;
          this.selectedUser = data;
          this.users.push(this.selectedUser);
          this.alertService.success('Teacher created successful');
          this.alertService.clearAfter(3000);
        },
        (error) => {
          this.localError = error as IError;
          this.alertService.error(this.localError.error.response);
        },

      );
    } else if (user.statut === 'Admin') {
      this.userService.createAdmin(user).subscribe(
        (data) => {
          this.display = false;
          this.selectedUser = data;
          console.log(this.users);
          this.users.push(this.selectedUser);
          console.log(this.users);
          this.alertService.success('Admin created successful');
          this.alertService.clearAfter(3000);
        },
        (error) => {
          this.localError = error as IError;
          this.alertService.error(this.localError.error.response);
        },

      );
    }
  }

  selectUser(user: User) {
    this.selectedUser = user;
    if ( this.selectedUser.statut === Role.Admin) {
      this.display = false;
    } else {
      this.display = true;
    }
  }

  addUser(statut: string) {
    this.display = true;
    this.newUser.email = '';
    this.newUser.firstName = '';
    this.newUser.lastName = '';
    this.newUser.mobile = '';
    this.newUser.password = '';
    this.newUser.role = '';
    this.newUser.statut = statut;
    this.selectedUser = this.newUser;
  }

}
