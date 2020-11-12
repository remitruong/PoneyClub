import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup,Validators } from '@angular/forms';
import { Router} from "@angular/router";
import {first} from "rxjs/operators";
import { User } from '../_classes';
import { IError } from '../_classes/ierror';
import { LoginModel } from '../_classes/loginmodel';
import { AlertService } from '../services/alert.service';
import { UserService } from '../services/api/user.service';
import {AuthenticationService} from "../services/authentification.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent  {

  @Output() public userConnected: EventEmitter<User> = new EventEmitter<User>();

  public user: User;
  public loginModel: LoginModel = {
    username: '',
    password: '',
  };
  public localError: IError;
  public submitted = false;
  public connectForm: FormGroup;

  constructor(private userService: UserService, private authenticationService: AuthenticationService,
              private alertService: AlertService, private router: Router, private formBuilder: FormBuilder) {

    // redirect to home if already logged in
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(['/home']);
    }
  }

  ngOnInit(): void {
    this.connectForm = this.formBuilder.group({
      emailOrPhone: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  get f() { return this.connectForm.controls; }

  public onSubmit(): void {
    this.submitted = true;

    if (this.connectForm.invalid) {
      return;
    }

    this.loginModel.username = this.connectForm.get('emailOrPhone').value;
    this.loginModel.password = this.connectForm.get('password').value;

    this.authenticationService.login(this.loginModel)
      .pipe(first())
      .subscribe(
        (data: HttpResponse<any>) => {
          this.alertService.success('You are connected', true);
          this.router.navigate(['/home']);
          this.alertService.clearAfter(1500);
        },
        (error) => {
          this.localError = error;
          this.alertService.error(this.localError.error.response);
        });

  }

}
