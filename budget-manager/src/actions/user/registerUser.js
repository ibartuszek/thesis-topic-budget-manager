import {createUserData} from "./createUserData";
import {createUserModelBody} from "./createUserModelBody";

export function registerUser(model) {
  let header = new Headers();
  header.set('Content-Type', 'application/json');
  let body = JSON.stringify(createUserModelBody(model));
  return function (dispatch) {
    return fetch(`/bm/users/register`, {
      method: 'POST',
      headers: header,
      body: body
    }).then(function (response) {
        if (!response.ok) {
          throw Error(response.statusText);
        }
        return response.json();
      }
    ).then((response) => {
      let userData = createUserData(response['userModel']);
      console.log('SIGN_UP_SUCCESS');
      dispatch({type: 'SIGN_UP_SUCCESS', userData: userData});
    }).catch(err => {
      console.log('SIGN_UP_ERROR');
      console.log(err);
      dispatch({type: 'SIGN_UP_ERROR'});
    });
  }
}

