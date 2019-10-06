import base64 from "react-native-base64";
import {createAccessCookie} from "./cookie/createAccessCookie";
import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithClientSecret} from "../common/createHeader";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages, userMessages} from "../../store/MessageHolder";

export function getAccessToken(username, password, messages) {
  const clientId = 'testjwtclientid';
  const clientSecret = 'XY7kmzoNzl100';
  let header = createHeaderWithClientSecret(clientId, clientSecret);
  let formData = createFromData(username, base64.encode(password));
  let successCase = 'GET_ACCESS_TOKEN_SUCCESS';
  let errorCase = 'GET_ACCESS_TOKEN_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(`oauth/token`, {
      method: 'POST',
      body: formData,
      headers: header
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let jwtToken = responseBody['access_token'];
        createAccessCookie(username, jwtToken);
        return dispatchSuccess(dispatchContext, null, 'jwtToken', jwtToken);
      } else if (responseStatus === 401) {
        return dispatchError(dispatchContext, userMessages['logInErrorMessage']);
      } else {
        return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
      }
    }).catch(errorMessage => {
      console.log(errorMessage);
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
