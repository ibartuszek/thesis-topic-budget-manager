export const formMessages = {
  emailLabel: "Email",
  emailMessage: "Please write your email address.",
  passwordLabel: "Password",
  passwordMessage: "Please write your password.",
  passwordConfirmMessage: "Please write your password again to confirm it.",
  firstNameLabel: "First name",
  firstNameMessage: "Please write your first name.",
  lastNameLabel: "Last name",
  lastNameMessage: "Please write your last name.",
};

export const userMessages = {
  logInMessage: "You have logged in successfully! Welcome!",
  logInErrorMessage: "Login failed, wrong username or password!",
  logOutMessage: "You have logged out. Please come again!",
  signUpMessage: "Your registration was successful.",
  signUpErrorMessage: "Your registration was not successful. This email has been used already!",
  updateUserMessage: "Your modification has been saved.",
  updateUserErrorMessage: "Your modification was not saved. This email has been used by another user.",
  defaultErrorMessage: "Something went wrong, please try again!",
};

export const validationMessages = {
  minimumMessage: "{} field must be minimum: {} character long!",
  maximumMessage: "{} field must be maximum: {} character long!",
  regexpMatchesMessage: "{} field must be add in a valid format!",
  enumValueMessage: "{} field must be one of them: {}!",
  passwordNotSameMessage: "The given passwords are not the same!"
};

export function replaceParams(message) {
  let messageFragments = message.split(/{}/);
  let newMessage = messageFragments[0];
  for (let index = 1; index < arguments.length && messageFragments.length; index++) {
    newMessage = newMessage.concat(arguments[index], messageFragments[index]);
  }
  return newMessage;
}
