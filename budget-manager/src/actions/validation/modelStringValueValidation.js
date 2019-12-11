import {replaceParams, validationMessages} from "../../store/MessageHolder";

export function validateModelStringValue(model, labelTitle, passwordValue) {
  const {passwordNotSameMessage} = validationMessages;
  let errorMessage = validateModelStringValueBase(model, labelTitle);
  if (model.value !== '' && errorMessage === null && passwordValue !== undefined) {
    errorMessage = model.value !== passwordValue ? passwordNotSameMessage : null;
  }
  return errorMessage;
}

function validateModelStringValueBase(model, labelTitle) {
  const {minimumMessage, maximumMessage, regexpMatchesMessage, enumValueMessage} = validationMessages;
  let errorMessage = null;
  if (model.value !== '') {
    if (model.minimumLength != null && model.value.length < model.minimumLength) {
      errorMessage = replaceParams(minimumMessage, labelTitle, model.minimumLength);
    } else if (model.maximumLength != null && model.value.length > model.maximumLength) {
      errorMessage = replaceParams(maximumMessage, labelTitle, model.maximumLength);
    } else if (model.regexp != null && !new RegExp(model.regexp).test(model.value)) {
      errorMessage = replaceParams(regexpMatchesMessage, labelTitle);
    } else if (model.possibleEnumValues != null && !model.possibleEnumValues.includes(model.value)) {
      errorMessage = replaceParams(enumValueMessage, labelTitle, model.possibleEnumValues);
    }
  }
  return errorMessage;
}
