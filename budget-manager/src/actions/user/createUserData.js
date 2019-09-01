export function createUserData(responseModel) {
  return {
    userId: responseModel['id'],
    userName: responseModel['email'].value,
    firstName: responseModel['firstName'].value,
    lastName: responseModel['lastName'].value,
  };
}
