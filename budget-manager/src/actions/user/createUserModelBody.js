import base64 from "react-native-base64";

export function createUserModelBody(userModel) {
  let password;
  if (userModel.password.value === '') {
    password = "********";
  } else {
    password = base64.encode(userModel.password.value);
  }

  return {
    id: userModel['id'],
    email: {
      value: userModel['email']['value']
    },
    password: {
      value: password
    },
    firstName: {
      value: userModel['firstName']['value']
    },
    lastName: {
      value: userModel['lastName']['value']
    }
  };
}
