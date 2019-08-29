import base64 from 'react-native-base64'

export function getAccessToken(body) {
  const clientId = 'testjwtclientid';
  const clientSecret = 'XY7kmzoNzl100';
  let header = createHeader(clientId, clientSecret);
  let formData = createFromData(body.username, body.password);

  return function (dispatch) {
    return fetch(`oauth/token`, {
      method: 'POST',
      body: formData,
      headers: header
    }).then(function (response) {
        if (!response.ok) {
          throw Error(response.statusText);
        }
        return response.json();
      }
    ).then(function (responseBody) {
      console.log("GET_ACCESS_TOKEN_SUCCESS");
      dispatch({type: 'GET_ACCESS_TOKEN_SUCCESS', jwtToken: responseBody['access_token']});
    }).catch(err => {
      console.log("GET_ACCESS_TOKEN_ERROR");
      dispatch({type: 'GET_ACCESS_TOKEN_ERROR', logInErrorMessage: err.message});
    });
  }
}

function createHeader(clientId, clientSecret) {
  let header = new Headers();
  header.set('Authorization', 'Basic ' + base64.encode(clientId + ':' + clientSecret));
  header.set('content-type', 'application/x-www-form-urlencoded;UTF-8');
  return header;
}

function createFromData(username, password) {
  let formData = [];
  formData.push("grant_type=password");
  formData.push("username=" + username);
  formData.push("password=" + password);
  formData.push("scopes=read write");
  return formData.join("&");
}
