import {Component, OnInit} from '@angular/core';
import { User } from '../_classes/user';
import { UserService } from '../services/api/user.service';
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {AlertService} from "../services/alert.service";

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

  constructor(private userService: UserService, private router: Router, private alertService: AlertService) {
  }

  ngOnInit(): void {
  }

  public onSubmit(): void {
    this.userService.signup(this.user).pipe(first()).subscribe(
      data => {
        // this.alertService.success('Sign up successful', true);
        alert(data);
        //Permet de rediriger si inscription
        this.router.navigate(['/login']);
      },
      error => {
        alert('error')
        // this.alertService.error('');
      }
    );
  }

}
