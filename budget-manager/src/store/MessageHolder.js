export const categoryMessages = {
  addNewSubCategory: "New subcategory",
  categoryNameLabel: "Name of the category",
  categoryNameMessage: "Please write the name of the new category.",
  createMainCategoryTitle: "Create new main category",
  createSubCategoryTitle: "Create new subcategory",
  selectNewCategory: "Choose one to add as a new category.",
  subCategoryLabel: "Subcategory",
  updateMainCategoryTitle: "Update main category",
  updateSubCategoryTitle: "Update subcategory",
};

export const defaultMessages = {
  defaultErrorMessage: "Something went wrong, please try again!",
};

export const statisticsMessages = {
  endDatePlaceHolder: "Select end date",
  startDatePlaceHolder: "Select end date"
};

export const transactionMessages = {
  createTransactionTitle: "Create new transaction",
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
  trackingLabel: "Outcome tracking",
  trackingMessage: "Please choose yes or no."
};

export const userMessages = {
  logInMessage: "User has logged in.",
  logInErrorMessage: "Login failed, wrong username or password!",
  logOutMessage: "User has logged out.",
  signUpMessage: "The registration was successful.",
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
