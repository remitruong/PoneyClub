import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthenticationService} from "../services/authentification.service";


@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const currentUser = this.authenticationService.currentUserValue;
    if (currentUser) {
      console.log("current user : " + currentUser.role +" " + currentUser.statut)
      if(route.data.roles && route.data.roles.indexOf(currentUser.role) === -1 ){
      // && route.data.status  && route.data.status.indexOf(currentUser.statut) === -1)
        this.router.navigate(['/'], { queryParams: { returnUrl: state.url }});
        return false;
      }
      return true;
    }

    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url }});
    return false;
  }

}
