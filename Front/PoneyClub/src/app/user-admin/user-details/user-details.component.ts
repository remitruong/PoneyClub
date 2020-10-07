import { Component, OnInit, Input, Output } from '@angular/core';
import { User } from 'src/app/_classes';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  @Input() userSelected:User;
  @Output() userUpdated: EventEmitter<User> = new EventEmitter<User>();
  @Output() userDeleted: EventEmitter<User> = new EventEmitter<User>();
  @Output() userAdded: EventEmitter<User> = new EventEmitter<User>();
  @Output() userToAdmin: EventEmitter<User> = new EventEmitter<User>();


  constructor() { }

  ngOnInit(): void {
  }

  updateUser() {
    this.userUpdated.emit(this.userSelected);
  }

  deleteUser() {
    this.userDeleted.emit(this.userSelected);
  }

  addUser() {
    this.userAdded.emit(this.userSelected);
  }

  changetoAdmin() {
    this.userToAdmin.emit(this.userSelected);
  }
}
