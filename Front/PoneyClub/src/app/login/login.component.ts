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
import {Role} from "../_classes/role";
import {Statut} from "../_classes/statut";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent  {

  @Output() public userConnected: EventEmitter<User> = new EventEmitter<User>();

  public loginModel: LoginModel = {
    username: '',
    password: '',
  };
  public localError: IError;
  public submitted = false;
  public connectForm: FormGroup;
  userTemp: User;

  constructor(private userService: UserService, private authenticationService: AuthenticationService,
              private alertService: AlertService, private router: Router, private formBuilder: FormBuilder) {

    if(this.authenticationService.currentUserValue){
      if(this.authenticationService.currentUserValue.statut==Statut.Admin){
        this.router.navigate(['/user-admin']);
      } else if(this.authenticationService.currentUserValue.statut==Statut.Root){
        this.router.navigate(['/super-admin']);
      } else if(this.authenticationService.currentUserValue.role==Role.Teacher){
        this.router.navigate(['/course']);
      }else{
        this.router.navigate(['/home']);
      }
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
        user => {
          this.alertService.success('You are connected', true);
          this.alertService.clearAfter(1500);
        });
  }

}
