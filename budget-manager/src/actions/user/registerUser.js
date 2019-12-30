import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {deleteAccessCookie} from "./cookie/deleteAccessCookie";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {transformUserFromResponse, transformUserToRequest} from "./createUserMethods";
import {defaultMessages} from "../../store/MessageHolder";

export function registerUser(model, messages) {
  let header = new Headers();
  header.set('Content-Type', 'application/json');
  let body = JSON.stringify(transformUserToRequest(model));
  let successCase = 'SIGN_UP_SUCCESS';
  let errorCase = 'SIGN_UP_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(process.env.REACT_APP_API_ENDPOINT + `/bm/users/register`, {
      method: 'POST',
      headers: header,
      body: body
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let userData = transformUserFromResponse(responseBody['user']);
        return dispatchSuccess(dispatchContext, responseBody['message'], 'userData', userData);
      } else if (responseStatus === 400 || responseStatus === 409 || responseStatus === 404) {
        return dispatchError(dispatchContext, responseBody);
      } else {
        deleteAccessCookie();
        return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
      }
    }).catch(errorMessage => {
      deleteAccessCookie();
      console.log(errorMessage);
    });
  }
}
