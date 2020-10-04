import { Component } from '@angular/core';
import { User } from './_classes';
import { ObjectService } from './services/object.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  user:User = {
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: ''
  };

  isConnected:boolean = true;

  constructor(private objectService : ObjectService) {}

  ngOnInit(): void {
    this.user = this.objectService.user;
    console.log(this.isConnected);
    if (this.user.id != null) {
      this.isConnected = true;
    }
  }

  disconnect() {
    console.log(this.user);
    this.user = null;
    this.isConnected = false;
    this.objectService.shUser(null);
  }

}

