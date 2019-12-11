import {createDispatchContext} from "../common/createDispatchContext";
import {createHeaderWithJwt} from "../common/createHeader";
import {createErrorBody} from "../common/createErrorBody";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function getPicture(pictureId, context) {
  const {userId, jwtToken, messages} = context;
  let url = `/bm/pictures/findById?id=` + pictureId + `&userId=` + userId;
  let header = createHeaderWithJwt(jwtToken);
  let successCase = 'GET_PICTURE_SUCCESS';
  let errorCase = 'GET_PICTURE_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(url, {
      method: 'GET',
      headers: header,
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let picture = responseBody['picture'];
        return dispatchSuccess(dispatchContext, responseBody['message'], 'picture', picture);
      } else if (responseStatus === 400 || responseStatus === 409 || responseStatus === 404) {
        return dispatchError(dispatchContext, responseBody);
      } else {
        return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
      }
    }).catch(errorMessage => {
      console.log(errorMessage);
    });
  }
}
