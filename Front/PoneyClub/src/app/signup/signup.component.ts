import {Component, OnInit} from '@angular/core';
import { User } from '../classes/user';
import { UserService } from '../services/api/user.service';
import {Router} from "@angular/router";
import {first} from "rxjs/operators";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit{
    user: User = {
    name: '',
    lastname:  '',
    mail: '',
    password: '',
    mobile: '',
    licencenum: ''
  }

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
  }

  public onSubmit(): void {
    this.userService.signup(this.user).pipe(first()).subscribe(
      data => {
        alert(data);
        this.router.navigate(['/login']);
      },
      error => {
        alert('error')
      }
    );
  }

}
