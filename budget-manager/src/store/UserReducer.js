const initState = {
  userIsLoggedIn: false,
  jwtToken: null,
  userData: null,
  logInMessage: null,
  logInErrorMessage: null,
  logOutMessage: null,
  logOutErrorMessage: null,
  signUpMessage: null,
  signUpErrorMessage: null,
  updateUserMessage: null,
  updateUserErrorMessage: null,
  errorMessage: null,
  message: null
};

const messages = {
  logInMessage: "You have logged in successfully! Welcome!",
  logInErrorMessage: "Login failed, wrong username or password!",
  logOutMessage: "You have logged out. Please come again!",
  signUpMessage: "Your registration was successful.",
  signUpErrorMessage: "Your registration was not successful. This email has been used already!",
  updateUserMessage: "Your registration was successful.",
  updateUserErrorMessage: "Your modification was not saved. This email has been used by another user.",
  defaultErrorMessage: "Something went wrong, please try again!",
};

const UserReducer = (state = initState, action) => {
  switch (action.type) {
    case 'GET_ACCESS_TOKEN_SUCCESS':
      return Object.assign({}, state, {
        jwtToken: action.jwtToken,
      });
    case 'GET_ACCESS_TOKEN_ERROR':
      return Object.assign({}, state, {
        jwtToken: null,
        userIsLoggedIn: false,
        logInMessage: null,
        logInErrorMessage: messages.logInErrorMessage
      });
    case 'LOGIN_SUCCESS':
      return Object.assign({}, state, {
        userData: action.userData,
        userIsLoggedIn: true,
        logInMessage: messages.logInMessage,
        logInErrorMessage: null
      });
    case 'LOGIN_ERROR':
      return Object.assign({}, state, {
        userData: null,
        userIsLoggedIn: false,
        logInMessage: null,
        logInErrorMessage: messages.logInErrorMessage
      });
    case 'LOGOUT_SUCCESS':
      return Object.assign({}, state, {
        userIsLoggedIn: false,
        jwtToken: null,
        logOutMessage: messages.logOutMessage,
        logOutErrorMessage: null,
        userData: null
      });
    case 'LOGOUT_ERROR':
      return Object.assign({}, state, {
        userData: null,
        userIsLoggedIn: false,
        logOutMessage: null,
        logOutErrorMessage: messages.defaultErrorMessage
      });
    case 'SIGN_UP_SUCCESS':
      return Object.assign({}, state, {
        userData: action.userData,
        userIsLoggedIn: true,
        signUpMessage: messages.signUpMessage,
        signUpErrorMessage: null
      });
    case 'SIGN_UP_ERROR':
      return Object.assign({}, state, {
        userData: null,
        userIsLoggedIn: false,
        signUpMessage: null,
        signUpErrorMessage: messages.signUpErrorMessage
      });
    case 'UPDATE_USER_SUCCESS':
      return Object.assign({}, state, {
        userData: action.userData,
        updateUserMessage: messages.updateUserMessage,
        updateUserErrorMessage: null
      });
    case 'UPDATE_USER_ERROR':
      return Object.assign({}, state, {
        updateUserMessage: null,
        updateUserErrorMessage: messages.updateUserErrorMessage
      });

    default:
      return state;
  }
};

export default UserReducer;
