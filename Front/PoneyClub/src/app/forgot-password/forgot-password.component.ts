import { Component, OnInit } from '@angular/core';
import {NbResetPasswordComponent} from "@nebular/auth";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent extends NbResetPasswordComponent implements OnInit {

  ngOnInit(): void {
  }
  // public requestPass(): void;

}
