import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../services/api/user.service";
import {Router} from "@angular/router";
import {AlertService} from "../services/alert.service";

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

  resetPasswordForm: FormGroup
  submitted = false;


  constructor(private userService: UserService, private router: Router, private alertService: AlertService, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
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
    alert("Submitted !")
  }



  // public requestPass(): void;

}
