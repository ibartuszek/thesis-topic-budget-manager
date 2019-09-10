export const categoryMessages = {
  addNewSubCategory: "New subcategory",
  categoryNameLabel: "Name of the category",
  categoryNameMessage: "Please write the name of the new category.",
  categoriesUnavailableMessage: "We cannot reach the backup repository, please try it later!",
  createMainCategorySuccess: "The category has been saved.",
  createMainCategoryError: "The category has been saved before.",
  createSubCategorySuccess: "The category has been saved.",
  createSubCategoryError: "The category has been saved before.",
  selectNewCategory: "Choose one to add as a new category.",
  subCategoryLabel: "Subcategory",
  updateMainCategorySuccess: "The category has been modified!",
  updateMainCategoryError: "The category exists!",
  updateSubCategorySuccess: "The category has been modified!",
  updateSubCategoryError: "The category exists!",
};

export const transactionMessages = {
  createTransactionSuccess: "The transaction has been saved.",
  createTransactionError: "The transaction has been saved before.",
  transactionAmountLabel: "Amount",
  transactionAmountMessage: "The amount of your new transaction.",
  transactionCurrencyLabel: "Currency",
  transactionCurrencyMessage: "The currency of your new transaction.",
  transactionMainCategoryLabel: "Main category",
  transactionMainCategoryMessage: "The main category of the transaction",
  transactionSubCategoryLabel: "Supplementary category",
  transactionSubCategoryMessage: "The supplementary category of the transaction",
  transactionTitleLabel: "Title",
  transactionTitleMessage: "The title of your new transaction."

};

export const userFormMessages = {
  emailLabel: "Email",
  emailMessage: "Please write your email address.",
  firstNameLabel: "First name",
  firstNameMessage: "Please write your first name.",
  lastNameLabel: "Last name",
  lastNameMessage: "Please write your last name.",
  passwordLabel: "Password",
  passwordMessage: "Please write your password.",
  passwordMessageToChange: "Please write your password to change.",
  passwordConfirmMessage: "Please write your password again to confirm.",
};

export const userMessages = {
  defaultErrorMessage: "Something went wrong, please try again!",
  logInMessage: "You have logged in successfully! Welcome!",
  logInErrorMessage: "Login failed, wrong username or password!",
  logOutMessage: "You have logged out. Please come again!",
  signUpMessage: "Your registration was successful.",
  signUpErrorMessage: "Your registration was not successful. This email has been used already!",
  updateUserMessage: "Your modification has been saved.",
  updateUserErrorMessage: "Your modification was not saved. This email has been used by another user.",
};

export const validationMessages = {
  enumValueMessage: "{} field must be one of them: {}!",
  maximumMessage: "{} field must be maximum: {} character long!",
  minimumMessage: "{} field must be minimum: {} character long!",
  passwordNotSameMessage: "The given passwords are not the same!",
  positiveMessage: "{} field must be positive!",
  positiveOrZeroMessage: "{} field cannot be negative!",
  regexpMatchesMessage: "{} field must be add in a valid format!",
};

export function replaceParams(message) {
  let messageFragments = message.split(/{}/);
  let newMessage = messageFragments[0];
  for (let index = 1; index < arguments.length && messageFragments.length; index++) {
    newMessage = newMessage.concat(arguments[index], messageFragments[index]);
  }
  return newMessage;
}
