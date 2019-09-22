import {createUserData} from "./createUserData";
import {createUserModelBody} from "./createUserModelBody";

export function registerUser(model, messages) {
  let header = new Headers();
  header.set('Content-Type', 'application/json');
  let body = createBody(model);
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
      dispatch({type: 'SIGN_UP_SUCCESS', userData: userData, messages: messages});
    }).catch(err => {
      console.log('SIGN_UP_ERROR');
      console.log(err);
      dispatch({type: 'SIGN_UP_ERROR', messages: messages});
    });
  }
}

function createBody(model) {
  let bodyObject = {
    userModel: createUserModelBody(model)
  };
  return JSON.stringify(bodyObject);
}
