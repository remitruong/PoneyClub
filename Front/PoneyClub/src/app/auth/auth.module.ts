import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import {NbAuthModule} from '@nebular/auth';
import {
  NbAlertModule,
  NbButtonModule, NbCardModule,
  NbCheckboxModule,
  NbInputModule, NbLayoutModule,
} from '@nebular/theme';
import {NgxLoginComponent} from '../login/login.component';
import { NgxAuthRoutingModule } from './auth-routing.module';
import {SignupComponent} from "../signup/signup.component";
import {ForgotPasswordComponent} from "../forgot-password/forgot-password.component";

@NgModule({
  declarations: [
    NgxLoginComponent,
    SignupComponent,
    ForgotPasswordComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    NbAlertModule,
    NbInputModule,
    NbButtonModule,
    NbCheckboxModule,
    NgxAuthRoutingModule,

    NbAuthModule,
    NbCardModule,
    NbLayoutModule,
  ],
})
export class NgxAuthModule {
}
