import {validateModelStringValue} from "./modelStringValueValidation";
import {validateModelAmountValue} from "./modelAmountValueValidation";
import {validateModelDateValue, validateModelEndDateValue} from "./modelDateValueValidation";

export function validateTransaction(transaction) {
  const {
    title, amount, currency, transactionType, mainCategory,
    date, monthly, endDate, description
  } = transaction;

  let valid = true;
  if (validateModelValue(title) || title.value === '' || validateModelStringValue(title) !== null) {
    valid = false;
  } else if (validateModelValue(amount) || validateModelAmountValue(amount) !== null) {
    valid = false;
  } else if (validateModelValue(currency) || validateModelStringValue(currency) !== null) {
    valid = false;
  } else if (validateModelValue(transactionType) || validateModelStringValue(transactionType) !== null) {
    valid = false;
  } else if (validateModelValue(mainCategory)) {
    valid = false;
  } else if (validateModelValue(date) || validateModelDateValue(date, "Date") !== null) {
    valid = false;
  } else if (monthly) {
    if (validateModelValue(endDate) || validateModelEndDateValue(endDate, date.value, "EndDate") !== null) {
      valid = false;
    }
  } else if (validateModelValue(description) || validateModelStringValue(description) !== null) {
    valid = false;
  }

  return valid;
}

function validateModelValue(field) {
  return field === undefined || field === null;
}

