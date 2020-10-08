import { Component, OnInit } from '@angular/core';
import { IHorse } from '../_classes/ihorse';
import { HorseService } from '../services/api/horse.service';
import { User } from '../_classes';
import { AuthenticationService } from '../services/authentification.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-horse',
  templateUrl: './horse.component.html',
  styleUrls: ['./horse.component.css']
})
export class HorseComponent implements OnInit {

  horse: IHorse = {
    id: 0,
    name: ''
  }
  horses: IHorse[] = [];
  currentUser: User = null;
  newHorsePanel = false;

  constructor(private horseService: HorseService, private authenticationService: AuthenticationService) { }

  ngOnInit(): void {

    this.currentUser = this.authenticationService.currentUserValue;

    this.horseService.getHorses(this.currentUser.id).subscribe(
      data => {
        this.horses = data;
      },
      error => {
        console.log("An error occured while retrieving horses");
      }
    )
  }

  addHorse() {
    this.newHorsePanel = true;
  }

  createHorse() {
    this.horseService.createHorse(this.horse.name, this.currentUser.id).subscribe(
      data => {
        this.horse = data;
        this.horses.push(this.horse);
        this.newHorsePanel = false;
      },
      error => {
        console.log("An error has occured while creating horse");
      }

    )
  }
  
  deleteHorse(horse: IHorse) {
    this.horseService.deleteHorse(horse, this.currentUser.id).subscribe(
      data => {
        let horseIndex = this.horses.indexOf(horse);
        this.horses.splice(horseIndex, 1);
        console.log("horse well deleted");
      },
      error => {
        console.log("error while deleting horse");
      }
    )
  }

  updateHorse(horse: IHorse){
    if (horse.id != null) {
      this.horseService.updateHorse(horse, this.currentUser.id).subscribe (
        data => {
          console.log("Horse name updated");
        },
        error => {
          console.log("Error occured while updating horse name");
        }
      )
    }
  }

}
