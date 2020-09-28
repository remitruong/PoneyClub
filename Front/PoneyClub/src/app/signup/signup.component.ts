import {Component, OnInit} from '@angular/core';
import { User } from '../classes/user';
import { ApiService } from '../services/api/api.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
    public user: User = {
    name: '',
    lastname:  '',
    mail: '',
    password: '',
    mobile: '',
    licencenum: '',
  };

  constructor(private apiService: ApiService) {
  }

  public ngOnInit(): void {
  }

  public register(): void {
    this.apiService.signup(this.user).subscribe(
      (res) => {
        alert(res);
      },
      (err) => {

      },
    );
  }

}
