import {addMessage, createMessage} from "../actions/message/messageActions";
import {userMessages} from "./MessageHolder"

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
    updateUserErrorMessage: null,
  }
};

const UserReducer = (state = initState, action) => {
  let key = null;
  switch (action.type) {
    case 'GET_ACCESS_TOKEN_SUCCESS':
      return Object.assign({}, state, {
        ...state,
        jwtToken: action.jwtToken,
      });
    case 'GET_ACCESS_TOKEN_ERROR':
      key = "logInErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        jwtToken: null,
        userIsLoggedIn: false,
        messages: {
          ...state.messages,
          logInMessage: null,
          logInErrorMessage: userMessages[key]
        }
      });
    case 'LOGIN_SUCCESS':
      key = "logInMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: action.userData,
        userIsLoggedIn: true,
        messages: {
          ...state.messages,
          logInMessage: userMessages[key],
          logInErrorMessage: null
        }
      });
    case 'LOGIN_ERROR':
      key = "logInErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: null,
        userIsLoggedIn: false,
        messages: {
          ...state.messages,
          logInMessage: null,
          logInErrorMessage: userMessages[key]
        }
      });
    case 'LOGOUT_SUCCESS':
      key = "logOutMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userIsLoggedIn: false,
        jwtToken: null,
        messages: {
          ...state.messages,
          logOutMessage: userMessages[key],
          logOutErrorMessage: null
        },
        userData: null
      });
    case 'LOGOUT_ERROR':
      key = "logOutErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: null,
        userIsLoggedIn: false,
        messages: {
          ...state.messages,
          logOutMessage: null,
          logOutErrorMessage: userMessages[key]
        }
      });
    case 'SIGN_UP_SUCCESS':
      key = "signUpMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: action.userData,
        userIsLoggedIn: true,
        messages: {
          ...state.messages,
          signUpMessage: userMessages[key],
          signUpErrorMessage: null,
        }
      });
    case 'SIGN_UP_ERROR':
      key = "signUpErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: null,
        userIsLoggedIn: false,
        messages: {
          ...state.messages,
          signUpMessage: null,
          signUpErrorMessage: userMessages[key],
        }
      });
    case 'UPDATE_USER_SUCCESS':
      key = "updateUserMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: action.userData,
        messages: {
          ...state.messages,
          updateUserMessage: userMessages[key],
          updateUserErrorMessage: null,
        }
      });
    case 'UPDATE_USER_ERROR':
      key = "updateUserErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state,
        messages: {
          ...state,
          updateUserMessage: null,
          updateUserErrorMessage: userMessages[key],
        }
      });
    default:
      return state;
  }
};

export default UserReducer;
