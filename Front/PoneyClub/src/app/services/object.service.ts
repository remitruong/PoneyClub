import { Injectable } from '@angular/core';
import { User } from '../_classes/user';

@Injectable({
  providedIn: 'root'
})
export class ObjectService {

  private currentUser:User = {
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: ''
  };

  constructor() { }

  get user():User {
    return this.currentUser;
  }

  shUser(user: User) {
    this.currentUser = user;
  }

}
