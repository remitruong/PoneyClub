import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'datetime'
})
export class DateTimeTostringPipe implements PipeTransform {

  transform(dateTime: string): string {
    const regex = /T/i;
    let date = new String(dateTime);
    date = date.replace(regex, ' ');
    date = date + ':00';
    date = date.slice(0, 19)
    dateTime = String(date);
    return dateTime;
  }

}
