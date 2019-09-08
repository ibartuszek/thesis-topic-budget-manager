export function createUserModelBody(userModel) {
  if (userModel.password.value === '') {
    userModel.password.value = "********";
  }

  return {
    userModel: {
      id: userModel['id'],
      email: {
        value: userModel['email']['value']
      },
      password: {
        value: userModel['password']['value']
      },
      firstName: {
        value: userModel['firstName']['value']
      },
      lastName: {
        value: userModel['lastName']['value']
      }
    }
  };
}
