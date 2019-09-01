import {validateModelStringValue} from "./modelStringValueValidation";

export function validateUserModel(userModel) {
  let firstInvalidObject = Object.keys(userModel).find(function (element) {
    if (userModel[element] !== null && userModel[element] !== undefined) {
      return userModel[element].value === '' || validateModelStringValue(userModel[element]) !== null;
    }
  });
  return firstInvalidObject == null;
}
