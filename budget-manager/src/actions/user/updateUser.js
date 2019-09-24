import {createUserFromResponse, createUserToRequest} from "./createUser";
import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";

export function updateUser(context, userModel) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(userModel, userId));

  return function (dispatch) {
    return fetch(`/bm/users/update`, {
      method: 'PUT',
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
      console.log('UPDATE_USER_SUCCESS');
      console.log(response);
      dispatch({type: 'UPDATE_USER_SUCCESS', userData: userData, messages: messages});
    }).catch(err => {
      console.log('UPDATE_USER_ERROR');
      console.log(err);
      dispatch({type: 'UPDATE_USER_ERROR', messages: messages});
    });
  }
}

function createBody(userModel, userId) {
  let body = {};
  body.user = createUserToRequest(userModel);
  body.userId = userId;
  return body;
}
