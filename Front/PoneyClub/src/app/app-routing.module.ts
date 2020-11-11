import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from "./login/login.component";
import {SignupComponent} from "./signup/signup.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {UserAdminComponent} from "./user-admin/user-admin.component";
import {HorseComponent} from './horse/horse.component';
import {CourseComponent} from './course/course.component';
import {AuthGuard} from "./_auth/auth.guard";
import {Role} from "./_classes/role";
import {Statut} from "./_classes/statut";
import {ResetPasswordComponent} from "./reset-password/reset-password.component";
import {AccountPageComponent} from "./account-page/account-page.component";
import {SuperAdminComponent} from "./super-admin/super-admin.component";

const routes: Routes = [

  {path: '', component: LoginComponent, pathMatch: 'full', canActivate: [AuthGuard] },
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
  {path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  {path: 'account', component: AccountPageComponent, canActivate: [AuthGuard] },
  {path: 'user-admin', component: UserAdminComponent, canActivate: [AuthGuard], data: {status: [Statut.Admin] }},
  //{path: 'super-admin', component: SuperAdminComponent, canActivate: [AuthGuard], data: {status: [Statut.SuperAdmin] }},
  {path: 'super-admin', component: SuperAdminComponent},
  {path: 'course', component: CourseComponent, canActivate: [AuthGuard] },
  {path: 'horse', component: HorseComponent, canActivate: [AuthGuard], data: {roles: [Role.Teacher, Role.Admin], status: [Statut.Admin, Statut.User] }},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
