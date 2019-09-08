import {addMessage, createMessage} from "../actions/message/messageActions";
import {userMessages} from "./MessageHolder"

const initState = {
  userIsLoggedIn: false,
  jwtToken: null,
  userData: null,
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
      });
    case 'LOGIN_SUCCESS':
      key = "logInMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: action.userData,
        userIsLoggedIn: true,
      });
    case 'LOGIN_ERROR':
      key = "logInErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: null,
        userIsLoggedIn: false,
      });
    case 'LOGOUT_SUCCESS':
      key = "logOutMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userIsLoggedIn: false,
        jwtToken: null,
        userData: null
      });
    case 'LOGOUT_ERROR':
      key = "logOutErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: null,
        userIsLoggedIn: false,
      });
    case 'SIGN_UP_SUCCESS':
      key = "signUpMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: action.userData,
        userIsLoggedIn: true,
      });
    case 'SIGN_UP_ERROR':
      key = "signUpErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: null,
        userIsLoggedIn: false,
      });
    case 'UPDATE_USER_SUCCESS':
      key = "updateUserMessage";
      addMessage(action.messages, createMessage(key, true, userMessages));
      return Object.assign({}, state, {
        ...state,
        userData: action.userData,
      });
    case 'UPDATE_USER_ERROR':
      key = "updateUserErrorMessage";
      addMessage(action.messages, createMessage(key, false, userMessages));
      return Object.assign({}, state, {
        ...state
      });
    default:
      return state;
  }
};

export default UserReducer;
