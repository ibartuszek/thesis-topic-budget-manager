import base64 from "react-native-base64";

const emailMinimumLength = 8;
const emailMaximumLength = 50;
const emailRegexp = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
const emailPossibleEnumValues = null;
const passwordMinimumLength = 8;
const passwordMaximumLength = 16;
const passwordRegexp = null;
const passwordPossibleEnumValues = null;
const firstNameMinimumLength = 2;
const firstNameMaximumLength = 50;
const firstNameRegexp = null;
const firstNamePossibleEnumValues = null;
const lastNameMinimumLength = 2;
const lastNameMaximumLength = 50;
const lastNameRegexp = null;
const lastNamePossibleEnumValues = null;

export function createUserEmptyUser() {
  return {
    userId: null,
    email: {
      value: '',
      errorMessage: null,
      minimumLength: emailMinimumLength,
      maximumLength: emailMaximumLength,
      regexp: emailRegexp,
      possibleEnumValues: emailPossibleEnumValues
    },
    password: {
      value: '',
      errorMessage: null,
      minimumLength: passwordMinimumLength,
      maximumLength: passwordMaximumLength,
      regexp: passwordRegexp,
      possibleEnumValues: passwordPossibleEnumValues
    },
    confirmationPassword: {
      value: '',
      errorMessage: null,
      minimumLength: passwordMinimumLength,
      maximumLength: passwordMaximumLength,
      regexp: passwordRegexp,
      possibleEnumValues: passwordPossibleEnumValues
    },
    firstName: {
      value: '',
      errorMessage: null,
      minimumLength: firstNameMinimumLength,
      maximumLength: firstNameMaximumLength,
      regexp: firstNameRegexp,
      possibleEnumValues: firstNamePossibleEnumValues
    },
    lastName: {
      value: '',
      errorMessage: null,
      minimumLength: lastNameMinimumLength,
      maximumLength: lastNameMaximumLength,
      regexp: lastNameRegexp,
      possibleEnumValues: lastNamePossibleEnumValues
    },
  };
}

export function createUserFromResponse(responseModel) {
  let user = createUserEmptyUser();
  user.userId = responseModel['id'];
  user.email.value = responseModel['email'];
  user.firstName.value = responseModel['firstName'];
  user.lastName.value = responseModel['lastName'];
  return user;
}

export function createUserToRequest(userModel) {
  let password;
  if (userModel.password.value === '') {
    password = "********";
  } else {
    password = base64.encode(userModel.password.value);
  }

  return {
    id: userModel['userId'],
    email: userModel['email'].value,
    password: password,
    firstName: userModel['firstName'].value,
    lastName: userModel['lastName'].value,
  };
}
