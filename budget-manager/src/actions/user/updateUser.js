import {createUserData} from "./createUserData";

export function updateUser(userModel, userId, jwtToken) {
  let header = createHeader(jwtToken);
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
      let userData = createUserData(response['userModel']);
      console.log('UPDATE_USER_SUCCESS');
      console.log(response);
      dispatch({type: 'UPDATE_USER_SUCCESS', userData: userData, message: response.message});
    }).catch(err => {
      console.log('UPDATE_USER_ERROR');
      console.log(err);
      dispatch({type: 'UPDATE_USER_ERROR', errorMessage: err.message});
    });
  }
}

function createHeader(jwtToken) {
  let header = new Headers();
  header.set('Content-Type', 'application/json');
  header.set('Authorization', 'Bearer ' + jwtToken);
  return header;
}

function createBody(userModel, userId) {
  if (userModel.password.value === '') {
    userModel.password.value = '********'
  }
  let body = {};
  body.userModel = userModel;
  body.userId = userId;
  return body;
}
