import moment from 'moment';

export function convertDate(date, dateFormat) {
  console.log(date === null ? null : moment(date).format(dateFormat));
  return date === null ? null : moment(date).format(dateFormat);
}
