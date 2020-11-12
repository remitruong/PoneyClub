import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'stringToDatetime'
})
export class StringToDatetimePipe implements PipeTransform {

  transform(datetimestring: string): string {
    let datetime = datetimestring;
    datetime = datetime.replace(' ', 'T');
    datetime = datetime.slice(0, 16);

    return datetime;
  }

}
