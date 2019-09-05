// TODO: make a post request to backend
export function logOut(messages) {
  console.log("LOGOUT_SUCCESS");
  return (dispatch) => {
    dispatch({type: 'LOGOUT_SUCCESS', messages: messages});
  }
}
