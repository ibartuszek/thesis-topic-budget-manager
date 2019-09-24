import {createUserFromResponse, createUserToRequest} from "./createUser";

export function registerUser(model, messages) {
  let header = new Headers();
  header.set('Content-Type', 'application/json');
  let body = JSON.stringify(createUserToRequest(model));
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
      let userData = createUserFromResponse(response['user']);
      console.log('SIGN_UP_SUCCESS');
      dispatch({type: 'SIGN_UP_SUCCESS', userData: userData, messages: messages});
    }).catch(err => {
      console.log('SIGN_UP_ERROR');
      console.log(err);
      dispatch({type: 'SIGN_UP_ERROR', messages: messages});
    });
  }
}
