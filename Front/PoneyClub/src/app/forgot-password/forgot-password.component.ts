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
  emailPattern ="([a-zA-Z0-9_.]{1,})((@[a-zA-Z]{2,})[\\\.]([a-zA-Z]{2}|[a-zA-Z]{3}))";

  constructor(private userService: UserService, private router: Router, private alertService: AlertService,
              private formBuilder: FormBuilder) {
  }

  public ngOnInit(): void {
    this.forgotPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
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
        this.alertService.success("Check your email to change your password");
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response);
      },

    );

  }

}
