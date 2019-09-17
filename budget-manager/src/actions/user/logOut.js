// TODO: make a post request to backend
import {deleteAccessCookie} from "./cookie/deleteAccessCookie";

export function logOut(messages) {
  console.log("LOGOUT_SUCCESS");
  return (dispatch) => {
    deleteAccessCookie();
    dispatch({type: 'LOGOUT_SUCCESS', messages: messages});
  }
}
