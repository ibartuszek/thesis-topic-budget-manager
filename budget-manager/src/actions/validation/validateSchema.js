import {validateModelStringValue} from "./modelStringValueValidation";

export function validateSchema(transaction) {
  const {chartType, currency, title, type} = transaction;

  let valid = true;
  if (validateModelValue(title) || title.value === '' || validateModelStringValue(title) !== null) {
    valid = false;
  } else if (validateModelValue(type) || validateModelStringValue(type) !== null) {
    valid = false;
  } else if (validateModelValue(currency) || validateModelStringValue(currency) !== null) {
    valid = false;
  } else if (validateModelValue(chartType) || validateModelStringValue(chartType) !== null) {
    valid = false;
  }
  return valid;
}

function validateModelValue(field) {
  return field === undefined || field === null;
}

