import {addMessage} from "../actions/message/messageActions";

const initState = {
  userIsLoggedIn: false,
  jwtToken: null,
  userData: null,
  messages: {
    logInMessage: null,
    logInErrorMessage: null,
    logOutMessage: null,
    logOutErrorMessage: null,
    signUpMessage: null,
    signUpErrorMessage: null,
    updateUserMessage: null,
    updateUserErrorMessage: null
  }
};

const defaultMessages = {
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
  let key = null;
  switch (action.type) {
    case 'GET_ACCESS_TOKEN_SUCCESS':
      return Object.assign({}, state, {
        jwtToken: action.jwtToken,
      });
    case 'GET_ACCESS_TOKEN_ERROR':
      key = "defaultErrorMessage";
      addMessage(action.messages, createMessage(key, false));
      return Object.assign({}, state, {
        jwtToken: null,
        userIsLoggedIn: false,
        messages: {
          logInMessage: null,
          logInErrorMessage: defaultMessages[key]
        }
      });
    case 'LOGIN_SUCCESS':
      key = "logInMessage";
      addMessage(action.messages, createMessage(key, true));
      return Object.assign({}, state, {
        userData: action.userData,
        userIsLoggedIn: true,
        messages: {
          logInMessage: defaultMessages[key],
          logInErrorMessage: null
        }
      });
    case 'LOGIN_ERROR':
      key = "logInErrorMessage";
      addMessage(action.messages, createMessage(key, false));
      return Object.assign({}, state, {
        userData: null,
        userIsLoggedIn: false,
        messages: {
          logInMessage: null,
          logInErrorMessage: defaultMessages[key]
        }
      });
    case 'LOGOUT_SUCCESS':
      key = "logOutMessage";
      addMessage(action.messages, createMessage(key, true));
      return Object.assign({}, state, {
        userIsLoggedIn: false,
        jwtToken: null,
        messages: {
          logOutMessage: defaultMessages[key],
          logOutErrorMessage: null
        },
        userData: null
      });
    case 'LOGOUT_ERROR':
      key = "logOutErrorMessage";
      addMessage(action.messages, createMessage(key, false));
      return Object.assign({}, state, {
        userData: null,
        userIsLoggedIn: false,
        messages: {
          logOutMessage: null,
          logOutErrorMessage: defaultMessages[key]
        }
      });
    case 'SIGN_UP_SUCCESS':
      key = "signUpMessage";
      addMessage(action.messages, createMessage(key, true));
      return Object.assign({}, state, {
        userData: action.userData,
        userIsLoggedIn: true,
        messages: {
          signUpMessage: defaultMessages[key],
          signUpErrorMessage: null
        }
      });
    case 'SIGN_UP_ERROR':
      key = "signUpErrorMessage";
      addMessage(action.messages, createMessage(key, false));
      return Object.assign({}, state, {
        userData: null,
        userIsLoggedIn: false,
        messages: {
          signUpMessage: null,
          signUpErrorMessage: defaultMessages[key]
        }
      });
    case 'UPDATE_USER_SUCCESS':
      key = "updateUserMessage";
      addMessage(action.messages, createMessage(key, true));
      return Object.assign({}, state, {
        userData: action.userData,
        messages: {
          updateUserMessage: defaultMessages[key],
          updateUserErrorMessage: null
        }
      });
    case 'UPDATE_USER_ERROR':
      key = "updateUserErrorMessage";
      addMessage(action.messages, createMessage(key, false));
      return Object.assign({}, state, {
        messages: {
          updateUserMessage: null,
          updateUserErrorMessage: defaultMessages[key]
        }
      });

    default:
      return state;
  }
};

function createMessage(key, success) {
  return {
    key: key,
    value: defaultMessages[key],
    success: success
  };
}

export default UserReducer;
