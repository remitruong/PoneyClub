import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { IError } from '../_classes/ierror';
import {AlertService} from "../services/alert.service";
import {UserService} from "../services/api/user.service";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent implements OnInit {

  public localError: IError;
  public forgotPasswordForm: FormGroup;
  public submitted = false;
  public emailPattern = '(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*' +
  '|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")' +
  '@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]' +
  '|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*' +
  '[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])';

  constructor(private userService: UserService, private router: Router, private alertService: AlertService,
              private formBuilder: FormBuilder) {
  }

  public ngOnInit(): void {
    this.forgotPasswordForm = this.formBuilder.group({
      email : ['', Validators.required, Validators.email],
    });
  }

  get f() { return this.forgotPasswordForm.controls; }

  public onSubmit(): void {
    this.submitted = true;

    if (this.forgotPasswordForm.invalid) {
      return;
    }

    this.userService.forgotPassword(this.forgotPasswordForm.get('email').value).subscribe(
      (data) => {
        this.alertService.success(data);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response);
      },

    );

  }

}
