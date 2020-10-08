import { Component } from '@angular/core';
import { User } from './_classes';
import {AuthenticationService} from "./services/authentification.service";
import {Router} from "@angular/router";
import {AlertService} from "./services/alert.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  currentUser: User;

  constructor(private authenticationService: AuthenticationService, private router: Router, private alertService: AlertService) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }

  ngOnInit(): void {
  }

  disconnect() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  refreshAlert(){
    setTimeout(() => {
      this.alertService.clear();
    }, 3000);
  }

  // backToHome(){
  //   window.location.reload();
  //   this.router.navigate(['/home']);
  // }

}

