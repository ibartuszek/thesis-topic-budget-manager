import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {deleteAccessCookie} from "./cookie/deleteAccessCookie";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {transformUserFromResponse, transformUserToRequest} from "./createUserMethods";
import {defaultMessages} from "../../store/MessageHolder";

export function updateUser(context, userModel) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(userModel, userId));
  let successCase = 'UPDATE_USER_SUCCESS';
  let errorCase = 'UPDATE_USER_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(`/bm/users/update`, {
      method: 'PUT',
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

function createBody(userModel, userId) {
  let body = {};
  body.user = transformUserToRequest(userModel);
  body.userId = userId;
  return body;
}
