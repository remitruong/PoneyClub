import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'datetime'
})
export class DateTimePipe implements PipeTransform {

  transform(dateTime: string): string {
    var regex = /T/i;
    var date = new String(dateTime);
    date = date.replace(regex, ' ');
    date = date + ':00';

    dateTime = String(date);
    
    return dateTime;
  }

}
