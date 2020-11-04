import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../services/api/user.service";
import {Router, ActivatedRoute} from "@angular/router";
import {AlertService} from "../services/alert.service";
import { IError } from '../_classes/ierror';

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
  }
}

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  localError: IError;
  resetPasswordForm: FormGroup
  submitted = false;
  newPassword: string;
  token: string;


  constructor(private userService: UserService, private router: Router, private alertService: AlertService,
     private formBuilder: FormBuilder, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(
      values => {
        this.token = values['token'];
      }
    )

    this.resetPasswordForm = this.formBuilder.group({
        password : ['', [Validators.required,  Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      }, {
        validator: MustMatch('password', 'confirmPassword')
      }
    )
  }

  get f() { return this.resetPasswordForm.controls; }


  public onSubmit() : void {
    this.submitted = true;

    if (this.resetPasswordForm.invalid) {
      return;
    }

    this.newPassword = this.resetPasswordForm.get('password').value;
    
    this.userService.setNewPassword(this.token, this.newPassword).subscribe(
      data => {
        this.router.navigate(['/login']);
      },
      error => {
        this.localError = error;
        this.alertService.error(this.localError.message);
      }

    )

  }



  // public requestPass(): void;

}
