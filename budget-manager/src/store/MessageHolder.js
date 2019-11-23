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

export const homeMessages = {
  homeIntroduction: "This application is made as a pet-project to develop a web-service. A registered user after sign-in can use its functionality. " +
    "It can store different type of incomes and expenses with unique categories and dates. The stored data can be shown in tables. " +
    "Different type of calculation can be executed on this transaction elements. This calculation runs on the fly on the backend services, " +
    "of which result can be shown different types of charts and tables.",
  homeExampleTransaction: "On this example can be shown how to manage incomes with categories and how to check them on a summarize table. " +
    "Categories can be modified, transactions can be modified and deleted as well. " +
    "Supplementary category cannot be removed from saved main categories. " +
    "Income categories can be applied on income transactions and outcome categories on outcome transactions. " +
    "Transactions has compulsory fields: title, amount, currency, main category and date; the other fields are optionals. " +
    "Monthly transaction can have end date, after its end date it won't be counted in statistics. " +
    "If tracking enabled frontend application will send your device coordinates to backend services, which will be saved on outcome data. " +
    "You can upload an shown pictures at outcomes.",
  homeExampleSchemas: "On this example can be shown how to manage schemas. They can be shown separately: standard, and custom schemas. The user can create" +
    "its own custom schema to specify what would like to show on his/her statistics. Standard statistics cannot be deleted or modified and it shows the " +
    "chosen period data on the basis of categories. Scale is a sum of all incomes reduced by the specified category (if there is not any category, the service" +
    "subtract all of them). Sum is a basic calculation where the main category is compulsory. (it can be both income and outcome).",
  homeExampleCalculateStatistics: "On this example can be shown how to create a statistics. The calculation is made on the basis of the schema type, the chosen" +
    "dates and the type of the chart. Because of the calculation is executed \"on-the-fly\" it can take a couple of seconds."
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
