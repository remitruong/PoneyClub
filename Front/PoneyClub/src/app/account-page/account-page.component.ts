import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../services/api/user.service';
import {AlertService} from '../services/alert.service';
import {User} from "../_classes";
import {AuthenticationService} from "../services/authentification.service";
import {IError} from "../_classes/ierror";
import { TokenStorageService } from '../_auth/token-storage.service';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-account-page',
  templateUrl: './account-page.component.html',
  styleUrls: ['./account-page.component.css']
})
export class AccountPageComponent  {

  submitted = false;
  accountForm: FormGroup;
  localError : IError;
  mobileNumberPattern = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";
  emailPattern ="([a-zA-Z0-9_.]{1,})((@[a-zA-Z]{2,})[\\\.]([a-zA-Z]{2}|[a-zA-Z]{3}))";

  currentUser: User;


  constructor(private userService: UserService,private authenticationService: AuthenticationService,
     private alertService: AlertService, private formBuilder: FormBuilder, private tokenService: TokenStorageService) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit(): void {
    this.accountForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', Validators.required],
      licenceNum: ['']
    });

  }

  get f() { return this.accountForm.controls; }

  updateUser() {
    this.submitted = true;
    if (this.accountForm.invalid) {
      return;
    }

    //replace info
    this.currentUser.email = this.accountForm.get('email').value;
    this.currentUser.mobile = this.accountForm.get('mobile').value;
    this.currentUser.licenceNum = this.accountForm.get('licenceNum').value;

    this.userService.updateUser(this.currentUser.id, this.currentUser).subscribe(
      data => {
        this.alertService.success('Update user successful');
        this.alertService.clearAfter(2000);
      },
      error => {
        console.log("error occured while update user" + error);
        this.alertService.error(this.localError.error.response);
      }
    )
  }

}
