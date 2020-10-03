import {Component, OnInit} from '@angular/core';
import { User } from '../_classes/user';
import { UserService } from '../services/api/user.service';
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {AlertService} from "../services/alert.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})

export class SignupComponent implements OnInit{
  signUpForm: FormGroup;
  submitted = false;

  user: User = {
    firstName: '',
    lastName:  '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: ''
  }

  constructor(private userService: UserService, private router: Router, private alertService: AlertService, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.signUpForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      mobile: ['', Validators.required],
      licenceNum: ['']
    });
  }

  get f() { return this.signUpForm.controls; }

  public onSubmit(): void {
    this.submitted = true;

    if (this.signUpForm.invalid) {
      return;
    }

    //bind data
    this.user.firstName = this.signUpForm.get('firstName').value;
    this.user.lastName = this.signUpForm.get('lastName').value;
    this.user.email = this.signUpForm.get('email').value;
    this.user.password = this.signUpForm.get('password').value;
    this.user.mobile = this.signUpForm.get('mobile').value;
    this.user.licenceNum = this.signUpForm.get('licenceNum').value;

    this.userService.signup(this.user).pipe(first()).subscribe(
      data => {
        alert(data);
        this.alertService.success('Sign up successful', true);
        this.router.navigate(['/login']);
      },
      error => {
        alert(error.toString())
        this.alertService.error(error.toString());
      }
    );
  }

  onReset(){
    this.submitted = false;
    this.signUpForm.reset();
  }

}
