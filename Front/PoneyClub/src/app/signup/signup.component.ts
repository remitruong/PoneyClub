import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {first} from 'rxjs/operators';
import {IError} from '../_classes/ierror';
import {User} from '../_classes/user';
import {AlertService} from '../services/alert.service';
import {UserService} from '../services/api/user.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})

export class SignupComponent implements OnInit {
  public signUpForm: FormGroup;
  public submitted = false;
  public localError: IError;
  public mobileNumberPattern = '^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$';
  emailPattern ="([a-zA-Z0-9_.]{1,})((@[a-zA-Z]{2,})[\\\.]([a-zA-Z]{2}|[a-zA-Z]{3}))";


  public user: User = {
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

  constructor(private userService: UserService, private router: Router, private alertService: AlertService, private formBuilder: FormBuilder) {
  }

  public ngOnInit(): void {
    this.signUpForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      mobile: ['', Validators.required],
      licenceNum: [''],
    });
  }

  get f() { return this.signUpForm.controls; }

  public onSubmit(): void {
    this.submitted = true;

    if (this.signUpForm.invalid) {
      return;
    }

    // bind data
    this.user.firstName = this.signUpForm.get('firstName').value;
    this.user.lastName = this.signUpForm.get('lastName').value;
    this.user.email = this.signUpForm.get('email').value;
    this.user.password = this.signUpForm.get('password').value;
    this.user.mobile = this.signUpForm.get('mobile').value;
    this.user.licenceNum = this.signUpForm.get('licenceNum').value;

    this.userService.signup(this.user).pipe(first()).subscribe(
      (data) => {
        this.alertService.success('Sign up successful', true);
        this.router.navigate(['/login']);
        this.alertService.clearAfter(1500);
      },
      (error) => {
        this.localError = error as IError;
        this.alertService.error(this.localError.error.response);
      },
    );
  }

  onReset() {
    this.submitted = false;
    this.signUpForm.reset();
  }

}
