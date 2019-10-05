import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwt} from "../common/createHeader";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {transformUserFromResponse} from "./createUserMethods";
import {defaultMessages, userMessages} from "../../store/MessageHolder";
import {deleteAccessCookie} from "./cookie/deleteAccessCookie";

export function getUser(userName, jwtToken, messages) {
  let url = `/bm/users/findByEmail?email=` + userName;
  let header = createHeaderWithJwt(jwtToken);
  let successCase = 'LOGIN_SUCCESS';
  let errorCase = 'LOGIN_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(url, {
      method: 'GET',
      headers: header
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let userData = transformUserFromResponse(responseBody['user']);
        return dispatchSuccess(dispatchContext, userMessages['logInMessage'], 'userData', userData);
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
