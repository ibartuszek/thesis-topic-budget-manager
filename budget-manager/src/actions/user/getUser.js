import {createUserData} from "./createUserData";
import {createHeaderWithJwt} from "../common/createHeader";
import {createAccessCookie} from "./cookie/createAccessCookie";

export function getUser(userName, jwtToken, messages) {
  let url = `/bm/users/findByEmail?email=` + userName;
  let header = createHeaderWithJwt(jwtToken);
  return function (dispatch) {
    return fetch(url, {
      method: 'GET',
      headers: header
    }).then(function (response) {
        if (!response.ok) {
          throw Error(response.statusText);
        }
        return response.json();
      }
    ).then((response) => {
      let userData = createUserData(response['userModel']);
      console.log('LOGIN_SUCCESS');
      createAccessCookie(userName, jwtToken);
      dispatch({type: 'LOGIN_SUCCESS', userData: userData, messages: messages});
    }).catch(err => {
      console.log('LOGIN_ERROR');
      console.log(err.message);
      dispatch({type: 'LOGIN_ERROR', messages: messages});
    });
  }
}
