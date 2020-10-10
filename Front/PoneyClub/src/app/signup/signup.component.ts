import {Component, OnInit} from '@angular/core';
import {User} from '../_classes/user';
import {UserService} from '../services/api/user.service';
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {AlertService} from "../services/alert.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {IError} from '../_classes/ierror';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})

export class SignupComponent implements OnInit{
  signUpForm: FormGroup;
  submitted = false;
  localError:IError;
  mobileNumberPattern = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";
  emailPattern ="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])";


  user: User = {
    id: 0,
    firstName: '',
    lastName:  '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: '',
    role:'',
    statut:''
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
        this.alertService.success('Sign up successful', true);
        this.router.navigate(['/login']);
        this.alertService.clearAfter(1500);
      },
      error => {
        this.localError = error as IError;
        this.alertService.error(this.localError.error.response);
      }
    );
  }

  onReset(){
    this.submitted = false;
    this.signUpForm.reset();
  }

}
