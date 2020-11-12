import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { IError } from '../_classes/ierror';
import {AlertService} from '../services/alert.service';
import {UserService} from '../services/api/user.service';

export function MustMatch(controlName: string, matchingControlName: string) {
  return (formGroup: FormGroup) => {
    const control = formGroup.controls[controlName];
    const matchingControl = formGroup.controls[matchingControlName];

    if (matchingControl.errors && !matchingControl.errors.mustMatch) {
      return;
    }
    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({ mustMatch: true });
    } else {
      matchingControl.setErrors(null);
    }
  };
}

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent implements OnInit {

  public localError: IError;
  public resetPasswordForm: FormGroup;
  public submitted = false;
  public newPassword: string;
  public token: string;

  constructor(private userService: UserService, private router: Router, private alertService: AlertService,
              private formBuilder: FormBuilder, private activatedRoute: ActivatedRoute) {
  }

  public ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(
      (values) => {
        this.token = values.token;
      },
    );

    this.resetPasswordForm = this.formBuilder.group({
        password : ['', [Validators.required,  Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      }, {
        validator: MustMatch('password', 'confirmPassword'),
      },
    );
  }

  get f() { return this.resetPasswordForm.controls; }

  public onSubmit(): void {
    this.submitted = true;

    if (this.resetPasswordForm.invalid) {
      return;
    }

    this.newPassword = this.resetPasswordForm.get('password').value;
    this.userService.setNewPassword(this.token, this.newPassword).subscribe(
      (data) => {
        this.alertService.success("Password changed successfull")
        this.router.navigate(['/login']);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response);
      },

    );

  }
}
