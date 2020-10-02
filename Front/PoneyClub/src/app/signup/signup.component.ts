import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { User } from '../classes/user';
import { ApiService } from '../services/api/api.service';
import {NbAuthComponent} from "@nebular/auth";
import {Router} from "@angular/router";

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

  constructor(private apiService: ApiService, private router: Router) {
  }

  ngOnInit(): void {
  }

  public register(): void {
    this.apiService.signup(this.user).subscribe(
      data => {
        alert(data);
        this.router.navigate(['/login']);
      },
      error => {
        alert('error');
      }
    );
  }

}
