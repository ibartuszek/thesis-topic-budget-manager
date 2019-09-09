import moment from 'moment';

export function convertDate(date, dateFormat) {
  const format = dateFormat.toUpperCase();
  return date === null ? null : moment(date).format(format);
}
