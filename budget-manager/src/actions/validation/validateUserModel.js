import {validateModelStringValue} from "./modelStringValueValidation";

export function validateUserModel(userModel, password, confirmationPassword) {
  let firstInvalidObject = Object.keys(userModel).find(function (element) {
    if (userModel[element] !== null && userModel[element] !== undefined) {
      let result;
      if ((element === 'password' && password !== undefined)
        || (element === 'confirmationPassword' && confirmationPassword !== undefined)) {
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
