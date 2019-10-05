import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwt} from "../common/createHeader";
import {deleteAccessCookie} from "./cookie/deleteAccessCookie";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function logOut(userId, jwtToken, messages) {
  let url = `/bm/users/logout?userId=` + userId;
  let header = createHeaderWithJwt(jwtToken);
  let successCase = 'LOGOUT_SUCCESS';
  let errorCase = 'LOGOUT_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(url, {
      method: 'POST',
      headers: header
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        deleteAccessCookie();
        return dispatchSuccess(dispatchContext, responseBody['message'], null, null);
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
