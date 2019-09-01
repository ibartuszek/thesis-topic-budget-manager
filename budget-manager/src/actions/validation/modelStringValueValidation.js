export function validateModelStringValue(model, labelTitle) {
  let errorMessage = null;
  if (model.value !== '') {
    if (model.minimumLength != null && model.value.length < model.minimumLength) {
      errorMessage = labelTitle + " field must be minimum: " + model.minimumLength + " character long!";
    } else if (model.maximumLength != null && model.value.length > model.maximumLength) {
      errorMessage = labelTitle + " field must be maximum: " + model.minimumLength + " character long!";
    } else if (model.regexp != null && !new RegExp(model.regexp).test(model.value)) {
      errorMessage = labelTitle + " field must be add in a valid format!";
    } else if (model.possibleEnumValues != null && model.possibleEnumValues.includes(model.value)) {
      // TODO: check
      errorMessage = labelTitle + " field must be one of them: " + model.possibleEnumValues;
    }
  }
  return errorMessage;
}
