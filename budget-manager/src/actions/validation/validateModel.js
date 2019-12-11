import {validateModelStringValue} from "./modelStringValueValidation";

export function validateModel(userModel, password) {
  let firstInvalidObject = Object.keys(userModel).find(function (element) {
    if (userModel[element] !== null && userModel[element] !== undefined) {
      let result;
      if ((element === 'password' || element === 'confirmationPassword') && password !== undefined) {
        result = null;
      } else {
        result = userModel[element].value === '' || validateModelStringValue(userModel[element]) !== null;
      }
      return result;
    }
    return null;
  });
  return firstInvalidObject == null;
}
