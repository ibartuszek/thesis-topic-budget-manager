import moment from 'moment';
import {dateProperties} from "../../store/Properties";

export function convertDate(date) {
  return date === null ? null : moment(date).format(dateProperties.dateFormat);
}
