import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import {NgbAlertModule, NgbPaginationModule} from '@ng-bootstrap/ng-bootstrap';

import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import {ApiService} from './services/api/api.service';
import { SignupComponent } from './signup/signup.component';

import { NbEvaIconsModule } from '@nebular/eva-icons';

import {
  NbButtonModule,
  NbCardModule,
  NbIconModule,
  NbLayoutModule,
  NbSidebarModule,
  NbThemeModule,
} from '@nebular/theme';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SignupComponent,
    LoginComponent,
    PageNotFoundComponent,
    ForgotPasswordComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NbThemeModule.forRoot({name: 'dark'}),
    NbLayoutModule,
    NbSidebarModule.forRoot(),
    NbButtonModule,
    NbIconModule,
    NbEvaIconsModule,
    NbButtonModule,
    NbCardModule,
    NgbModule,
    NgbPaginationModule,
    NgbAlertModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
