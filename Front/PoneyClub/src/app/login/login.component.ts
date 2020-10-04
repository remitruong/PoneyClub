import { Component, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder,Validators } from '@angular/forms';
import { User } from '../_classes';
import { Router} from "@angular/router";
import { UserService } from '../services/api/user.service';
import { AlertService } from '../services/alert.service';
import { IError } from '../_classes/ierror';
import { ObjectService } from '../services/object.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent  {

  @Output() userConnected: EventEmitter<User> = new EventEmitter<User>();

  user:User = {
    id: '',
    firstName: '',
    lastName:  '',
    email: '',
    password: '',
    mobile: '',
    licenceNum: ''
  }
  localError:IError;
  submitted = false;
  connectForm: FormGroup;

  constructor(private userService:UserService, private alertService: AlertService, private router: Router, private formBuilder: FormBuilder,
    private objectService: ObjectService){}

  ngOnInit(): void {
    this.connectForm = this.formBuilder.group({
      id: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get f() { return this.connectForm.controls; }

  public onSubmit():void {
    this.submitted = true;

    this.user.email = this.connectForm.get('id').value;
    this.user.mobile = this.connectForm.get('id').value;
    this.user.password = this.connectForm.get('password').value;

    this.userService.connect(this.user).subscribe(
      data => {
        this.user = data;
        this.objectService.shUser(this.user);
        this.alertService.success('You are connected', true);
        this.router.navigate(['/home']);
      },
      error => {
        this.localError = error;
        this.alertService.error(this.localError.error.response);
      }

    )



  }

}
