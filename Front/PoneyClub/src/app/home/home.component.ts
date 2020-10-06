import {Component, OnInit, Input} from '@angular/core';
import { User } from "../_classes/user";
import {AuthenticationService} from "../services/authentification.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {


  currentUser: User;

  constructor(private authenticationService: AuthenticationService) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit(): void {
  }

}
