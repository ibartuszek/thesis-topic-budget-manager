export const categoryMessages = {
  addNewSubCategory: "New subcategory",
  categoryNameLabel: "Name of the category",
  categoryNameMessage: "Please write the name of the new category.",
  categoriesUnavailableMessage: "We cannot reach the backup repository, please try it later!",
  createMainCategorySuccess: "The category has been saved.",
  createMainCategoryError: "The category has been saved before.",
  createMainCategoryTitle: "Create new main category",
  createSubCategorySuccess: "The category has been saved.",
  createSubCategoryError: "The category has been saved before.",
  createSubCategoryTitle: "Create new subcategory",
  selectNewCategory: "Choose one to add as a new category.",
  subCategoryLabel: "Subcategory",
  updateMainCategorySuccess: "The category has been modified!",
  updateMainCategoryError: "The category exists!",
  updateMainCategoryTitle: "Update main category",
  updateSubCategorySuccess: "The category has been modified!",
  updateSubCategoryError: "The category exists!",
  updateSubCategoryTitle: "Update subcategory",
};

export const defaultMessages = {
  defaultErrorMessage: "Something went wrong, please try again!",
};

export const transactionMessages = {
  createTransactionSuccess: "The transaction has been saved.",
  createTransactionError: "The transaction has been saved before.",
  createTransactionTitle: "Create new transaction",
  deleteTransactionSuccess: "The transaction has been deleted.",
  deleteTransactionError: "The transaction cannot be deleted.",
  transactionAmountLabel: "Amount",
  transactionAmountMessage: "The amount of your new transaction.",
  transactionCurrencyLabel: "Currency",
  transactionCurrencyMessage: "The currency of your new transaction.",
  transactionDateLabel: "Date",
  transactionDateMessage: "Click to select the date of transaction.",
  transactionDescriptionLabel: "Description",
  transactionDescriptionMessage: "Description of the transaction.",
  transactionEndDateLabel: "End date",
  transactionEndDateMessage: "Click to select the end of monthly transaction.",
  transactionMainCategoryLabel: "Main category",
  transactionMainCategoryMessage: "The main category of the transaction",
  transactionMonthlyLabel: "Monthly transaction",
  transactionMonthlyMessage: "",
  transactionSubCategoryLabel: "Supplementary category",
  transactionSubCategoryMessage: "The supplementary category of the transaction",
  transactionTitleLabel: "Title",
  transactionTitleMessage: "The title of your new transaction.",
  transactionsEndDateLabel: "End date",
  transactionsEndDatePlaceHolder: "Select end date",
  transactionsStartDateLabel: "Start date",
  transactionsStartDatePlaceHolder: "Select start date",
  transactionsUnavailableMessage: "We cannot reach the backup repository, please try it later!",
  updateTransactionSuccess: "The transaction has been modified!",
  updateTransactionError: "The transaction exists!",
  updateTransactionTitle: "Update transaction",
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
  dateMustBeAfter: "{} cannot be before: {}!",
  datePatternErrorMessage: "{} must be given in {} format!",
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
