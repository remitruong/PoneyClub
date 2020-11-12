import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AlertComponent} from '../_alert/alert.component';
import { User } from '../_classes';
import { IHorse } from '../_classes/ihorse';
import {AlertService} from '../services/alert.service';
import { HorseService } from '../services/api/horse.service';
import { AuthenticationService } from '../services/authentification.service';
import { IError } from '../_classes/ierror';

@Component({
  selector: 'app-horse',
  templateUrl: './horse.component.html',
  styleUrls: ['./horse.component.css'],
})
export class HorseComponent implements OnInit {

  public horse: IHorse = {
    id: 0,
    name: '',
  };
  public newHorse: IHorse = {
    id: 0,
    name: '',
  };
  public horses: IHorse[] = [];
  public currentUser: User = null;
  public newHorsePanel = false;
  public submitted = false;
  public horseFormCreation: FormGroup;
  private localError: IError;

  constructor(private horseService: HorseService, private authenticationService: AuthenticationService,
              private alertService: AlertService, private formBuilder: FormBuilder) { }

  public ngOnInit(): void {
    this.currentUser = this.authenticationService.currentUserValue;
    this.submitted = false;
    this.horseFormCreation = this.formBuilder.group({
      horseNameCreation: ['', Validators.required],
    });
    this.horseService.getHorses().subscribe(
      (data) => {
        this.horses = data;
        this.alertService.success('All horse refreshed');
        this.alertService.clearAfter(1500);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response)
      },
    );
  }

  addHorse() {
    this.newHorse.id = 0;
    this.newHorse.name = '';
    this.newHorsePanel = true;
  }

  createHorse() {
    this.submitted = true;

    if (this.horseFormCreation.invalid) {
      return;
    }
    this.horseService.createHorse(this.newHorse.name, this.currentUser.id).subscribe(
      (data) => {
        this.horse = data;
        this.horses.push(this.horse);
        this.newHorsePanel = false;
        this.horseFormCreation.reset();
        this.alertService.success('Horse created success');
        this.alertService.clearAfter( 2000);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response)
      },
    );
    this.submitted = false;
  }

  deleteHorse(horse: IHorse) {
    this.horseService.deleteHorse(horse, this.currentUser.id).subscribe(
      (data) => {
        const horseIndex = this.horses.indexOf(horse);
        this.horses.splice(horseIndex, 1);
        this.alertService.success('Horse delete success');
        this.alertService.clearAfter(1500);
      },
      (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response)
      },
    );
  }

  updateHorse(horse: IHorse) {
    if (horse.id != 0) {
      this.horseService.updateHorse(horse, this.currentUser.id).subscribe (
        (data) => {
          this.alertService.success('Horse name updated');
          this.alertService.clearAfter(3000);
        },
        (error) => {
        this.localError = error;
        this.alertService.error(this.localError.error.response)
        },
      );
    }
  }

  get fC() { return this.horseFormCreation.controls; }

}
