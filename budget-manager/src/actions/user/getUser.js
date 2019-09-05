import {createUserData} from "./createUserData";

export function getUser(userName, jwtToken, messages) {
  let url = `/bm/users/findByEmail?email=` + userName;
  let header = createHeader(jwtToken);
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
      dispatch({type: 'LOGIN_SUCCESS', userData: userData, messages: messages});
    }).catch(err => {
      console.log('LOGIN_ERROR');
      console.log(err.message);
      dispatch({type: 'LOGIN_ERROR', messages: messages});
    });
  }
}

function createHeader(jwtToken) {
  let header = new Headers();
  header.set('Authorization', 'Bearer ' + jwtToken);
  return header;
}
