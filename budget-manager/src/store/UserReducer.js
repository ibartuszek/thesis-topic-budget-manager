const initState = {
  userIsLoggedIn: false,
  jwtToken: null,
  logInErrorMessage: null,
  userData: null
};

const UserReducer = (state = initState, action) => {
  switch (action.type) {
    case 'GET_ACCESS_TOKEN_SUCCESS':
      return Object.assign({}, state, {
        jwtToken: action.jwtToken,
        logInErrorMessage: null
      });
    case 'LOGIN_SUCCESS':
      return Object.assign({}, state, {
        userData: action.userData,
        userIsLoggedIn: true,
        logInErrorMessage: null
      });
    case 'GET_ACCESS_TOKEN_ERROR':
    case 'LOGIN_ERROR':
      return Object.assign({}, state, {
        logInErrorMessage: action.logInErrorMessage,
        userIsLoggedIn: false,
        jwtToken: null,
        userData: null
      });
    case 'LOGOUT_SUCCESS':
      return Object.assign({}, state, {
        userIsLoggedIn: false,
        jwtToken: null,
        logInErrorMessage: null,
        userData: null
      });
    default:
      return state;
  }
};

export default UserReducer;
