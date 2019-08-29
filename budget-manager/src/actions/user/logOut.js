// TODO: make a post request to backend
export function logOut() {
  console.log("LOGOUT_SUCCESS");
  return (dispatch) => {
    dispatch({type: 'LOGOUT_SUCCESS', userIsLoggedIn: false, jwtToken: null});
  }
}
