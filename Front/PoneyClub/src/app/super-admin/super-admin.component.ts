import {Component, OnInit} from '@angular/core';
import {UserService} from '../services/api/user.service';
import {User} from '../_classes';
import {IError} from '../_classes/ierror';
import {AlertService} from '../services/alert.service';
import {AuthenticationService} from '../services/authentification.service';

@Component({
  selector: 'app-super-admin',
  templateUrl: './super-admin.component.html',
  styleUrls: ['./super-admin.component.css']
})
export class SuperAdminComponent implements OnInit {

  newAdmin: User = {
    id: 0,
    firstName: '',
    lastName:  '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: '',
    role:'',
    statut:'',
  };
  admins: User[] = [];
  localError : IError;
  selectedAdmin: User;
  searchText;
  display = false;


  constructor(private userService : UserService, private alertService : AlertService, private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.getAllUsers();
  }


  getAllUsers() {
    this.userService.getUsers(this.authenticationService.currentUserValue.email).subscribe(
      data => {
        console.log(data);
        this.admins = data;
        this.alertService.success('All user refreshed');
        this.alertService.clearAfter(1500);

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
        this.alertService.clearAfter(3000);
      },
      error => {
        console.log("error occured while update user" + error);
        this.alertService.error(this.localError.error.response);
      }
    )
  }

  createAdmin(user: User) {
    this.userService.createAdmin(user).subscribe(
      data => {
        this.selectedAdmin = data;
        this.admins.push(this.selectedAdmin);
        this.display = false;
        this.getAllUsers();
        this.alertService.success('Admin created successful');
        this.alertService.clearAfter(3000);
      },
      error => {
        this.localError = error as IError;
        this.alertService.error(this.localError.error);
      }
    )
  }


  selectUser(user: User) {
    this.selectedAdmin = user;
    this.display = true;
  }

  addAdmin() {
    this.display = !this.display; //si on veut afficher/cacher sur le click
    this.newAdmin.email='';
    this.newAdmin.firstName='';
    this.newAdmin.lastName='';
    this.newAdmin.mobile='';
    this.newAdmin.password='';
    this.newAdmin.role='';
    this.newAdmin.statut='';
    this.selectedAdmin=this.newAdmin;
  }

}
