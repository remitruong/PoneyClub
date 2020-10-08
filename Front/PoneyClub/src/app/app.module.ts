import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

import {NgbAlertModule, NgbModule, NgbPaginationModule} from '@ng-bootstrap/ng-bootstrap';

import {HomeComponent} from './home/home.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';

import {NbEvaIconsModule} from '@nebular/eva-icons';

import {
  NbActionsModule,
  NbButtonModule,
  NbCardModule,
  NbIconModule,
  NbLayoutModule,
  NbThemeModule,
} from '@nebular/theme';
import {LoginComponent} from "./login/login.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {SignupComponent} from "./signup/signup.component";
import {AlertComponent} from "./_alert/alert.component";
import { UserAdminComponent } from './user-admin/user-admin.component';
import { UserDetailsComponent } from './user-admin/user-details/user-details.component';
import { CourseComponent } from './course/course.component';
import { HorseComponent } from './horse/horse.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ForgotPasswordComponent,
    SignupComponent,
    HomeComponent,
    PageNotFoundComponent,
    AlertComponent,
    UserAdminComponent,
    UserDetailsComponent,
    CourseComponent,
    HorseComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NbThemeModule.forRoot({name: 'dark'}),
    NbLayoutModule,
    NbIconModule,
    NbEvaIconsModule,
    NbCardModule,
    NgbModule,
    NbButtonModule,
    NgbPaginationModule,
    NgbAlertModule,
    NbActionsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
