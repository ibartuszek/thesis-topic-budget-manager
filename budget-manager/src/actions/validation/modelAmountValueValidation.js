import {replaceParams, validationMessages} from "../../store/MessageHolder";

export function validateModelAmountValue(model, labelTitle) {
  const {positiveMessage, positiveOrZeroMessage} = validationMessages;
  let errorMessage = null;
  if (model.value !== '') {
    if (model.positive != null && model.positive !== false && model.value <= 0) {
      errorMessage = replaceParams(positiveMessage, labelTitle);
    } else if (model.positiveOrZero != null && model.positiveOrZero !== false && model.value < 0) {
      errorMessage = replaceParams(positiveOrZeroMessage, labelTitle, model.maximumLength);
    }
  }
  return errorMessage;
}
