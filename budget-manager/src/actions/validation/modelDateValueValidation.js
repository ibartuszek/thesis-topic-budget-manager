import moment from "moment";
import {replaceParams, validationMessages} from "../../store/MessageHolder";

export function validateModelDateValue(date, dateFormat, labelTitle) {
  const {datePatternErrorMessage, dateMustBeAfter} = validationMessages;
  let errorMessage = null;
  if (date !== undefined && date !== null && date.value !== null) {
    if (!moment(date.value, dateFormat, true).isValid()) {
      errorMessage = replaceParams(datePatternErrorMessage, labelTitle, dateFormat)
    } else {
      let currentDateValue = moment(date.value);
      let possibleFirstDateValue = moment(date.possibleFirstDay);
      let possibleFirstDateValueText = possibleFirstDateValue.format(dateFormat);
      errorMessage = currentDateValue.isBefore(possibleFirstDateValue)
        ? replaceParams(dateMustBeAfter, labelTitle, possibleFirstDateValueText)
        : null;
    }
  }
  return errorMessage;
}
