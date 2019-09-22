import base64 from "react-native-base64";
import {createHeaderWithClientSecret} from "../common/createHeader";

export function getAccessToken(username, password, messages) {
  const clientId = 'testjwtclientid';
  const clientSecret = 'XY7kmzoNzl100';
  let header = createHeaderWithClientSecret(clientId, clientSecret);
  let formData = createFromData(username, base64.encode(password));

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
      console.log(err.message);
      dispatch({type: 'GET_ACCESS_TOKEN_ERROR', messages: messages});
    });
  }
}


function createFromData(username, password) {
  let formData = [];
  formData.push("grant_type=password");
  formData.push("username=" + username);
  formData.push("password=" + password);
  formData.push("scopes=read write");
  return formData.join("&");
}
