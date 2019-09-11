import moment from 'moment';

export function convertDate(date, dateFormat) {
  return date === null ? null : moment(date).format(dateFormat);
}
