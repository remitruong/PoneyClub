import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../services/api/user.service';
import {AlertService} from '../services/alert.service';
import {User} from "../_classes";
import {AuthenticationService} from "../services/authentification.service";

@Component({
  selector: 'app-account-page',
  templateUrl: './account-page.component.html',
  styleUrls: ['./account-page.component.css']
})
export class AccountPageComponent  {

  submitted = false;
  accountForm: FormGroup;
  mobileNumberPattern = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";
  emailPattern ="([a-zA-Z0-9_.]{1,})((@[a-zA-Z]{2,})[\\\.]([a-zA-Z]{2}|[a-zA-Z]{3}))";

  currentUser: User;


  constructor(private userService:UserService,private authenticationService: AuthenticationService, private alertService: AlertService, private formBuilder: FormBuilder){
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

  updateUser(){
    this.submitted = true;
    if (this.accountForm.invalid) {
      return;
    }

    //UPDATE USER


  }

}
