import moment from "moment";
import {replaceParams, validationMessages} from "../../store/MessageHolder";
import {dateProperties} from "../../store/Properties";

export function validateModelDateValue(date, labelTitle) {
  const {datePatternErrorMessage, dateMustBeAfter} = validationMessages;
  const dateFormat = dateProperties.dateFormat;
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
