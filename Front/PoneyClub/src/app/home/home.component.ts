import {Component, OnInit, Input} from '@angular/core';
import { User } from "../_classes/user";
import { ObjectService } from '../services/object.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  user : User = {
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: ''
  }

  isConnected = false;

  constructor(private objectService : ObjectService) { }

  ngOnInit(): void {
    this.user = this.objectService.user;
    if (this.user != null) { this.isConnected = true;}
  }

}
