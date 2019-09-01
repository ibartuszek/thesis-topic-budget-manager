export function registerUser(model) {
  let header = new Headers();
  header.set('Content-Type', 'application/json');
  let body = JSON.stringify(createBody(model));
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
      console.log(response);
      dispatch({type: 'SIGN_UP_SUCCESS', userData: userData});
    }).catch(err => {
      console.log('SIGN_UP_ERROR');
      console.log(err);
      dispatch({type: 'SIGN_UP_ERROR', logInErrorMessage: err.message});
    });
  }
}

function createBody(model) {
  return {
    userModel: {
      id: null,
      email: {
        value: model['email']['value']
      },
      password: {
        value: model['password']['value']
      },
      firstName: {
        value: model['firstName']['value']
      },
      lastName: {
        value: model['lastName']['value']
      }
    }
  };
}

function createUserData(responseModel) {
  return {
    userId: responseModel['id'],
    userName: responseModel['email'].value,
    firstName: responseModel['firstName'].value,
    lastName: responseModel['lastName'].value,
  };
}
