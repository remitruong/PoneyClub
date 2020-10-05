import {Component, OnInit, Input} from '@angular/core';
import { User } from "../_classes/user";
import { ObjectService } from '../services/object.service';
import {AuthenticationService} from "../services/authentification.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  user : User = {
    emailOrPhone: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: ''
  }

  currentUser: User;

  constructor(private authenticationService: AuthenticationService) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit(): void {
  }

}
