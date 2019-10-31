import moment from "moment";
import {replaceParams, validationMessages} from "../../store/MessageHolder";
import {dateProperties} from "../../store/Properties";

export function validateModelDateValue(date, labelTitle) {
  let errorMessage = null;
  if (date !== undefined && date !== null && date.value !== null) {
    errorMessage = validate(date, errorMessage, date.possibleFirstDay);
  }
  return errorMessage;
}

export function validateModelEndDateValue(endDate, possibleFirstDay, labelTitle) {
  let errorMessage = null;
  if (endDate !== undefined && endDate !== null && endDate.value !== null) {
    errorMessage = validate(endDate, labelTitle, possibleFirstDay);
  }
  return errorMessage;
}

function validate(date, labelTitle, firstPossibleDay) {
  const {datePatternErrorMessage, dateMustBeAfter} = validationMessages;
  const dateFormat = dateProperties.dateFormat;
  let errorMessage;
  if (!moment(date.value, dateFormat, true).isValid()) {
    errorMessage = replaceParams(datePatternErrorMessage, labelTitle, dateFormat)
  } else {
    let currentDateValue = moment(date.value);
    let possibleFirstDateValue = moment(firstPossibleDay);
    let possibleFirstDateValueText = possibleFirstDateValue.format(dateFormat);
    errorMessage = currentDateValue.isBefore(possibleFirstDateValue)
      ? replaceParams(dateMustBeAfter, labelTitle, possibleFirstDateValueText)
      : null;
  }
  return errorMessage;
}
