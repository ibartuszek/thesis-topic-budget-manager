const initState = {
  userIsLoggedIn: false,
  jwtToken: null,
  signUpMessage: null,
  message: null,
  errorMessage: null,
  userData: null
};

const UserReducer = (state = initState, action) => {
  switch (action.type) {
    case 'GET_ACCESS_TOKEN_SUCCESS':
      return Object.assign({}, state, {
        jwtToken: action.jwtToken,
        // message: null,
        errorMessage: null
      });
    case 'LOGIN_SUCCESS':
      return Object.assign({}, state, {
        userData: action.userData,
        userIsLoggedIn: true,
        message: action.message,
        errorMessage: null
      });
    case 'LOGOUT_SUCCESS':
      return Object.assign({}, state, {
        userIsLoggedIn: false,
        jwtToken: null,
        message: null,
        errorMessage: null,
        userData: null
      });
    case 'SIGN_UP_SUCCESS':
      return Object.assign({}, state, {
        userData: action.userData,
        userIsLoggedIn: true,
        message: action.message,
        errorMessage: null
      });
    case 'UPDATE_USER_SUCCESS':
      return Object.assign({}, state, {
        userData: action.userData,
        message: action.message,
        errorMessage: null
      });
    case 'GET_ACCESS_TOKEN_ERROR':
    case 'LOGIN_ERROR':
    case 'SIGN_UP_ERROR':
      return Object.assign({}, state, {
        errorMessage: action.errorMessage,
        userIsLoggedIn: false,
        jwtToken: null,
        userData: null
      });
    default:
      return state;
  }
};

export default UserReducer;
