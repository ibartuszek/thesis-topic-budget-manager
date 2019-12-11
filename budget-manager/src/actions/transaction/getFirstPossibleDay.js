import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {dispatchError} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function getFirstPossibleDay(context) {
  const {jwtToken, messages, userId} = context;
  let url = '/bm/transactions/getFirstPossibleDay?userId=' + userId;

  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let successCase = 'GET_FIRST_POSSIBLE_DAY_SUCCESS';
  let errorCase = 'GET_FIRST_POSSIBLE_DAY_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(url, {
      method: 'GET',
      headers: header
    }).then(function (response) {
      responseStatus = response.status;
      return responseStatus === 200 ? response.json() : createErrorBody(response);
    }).then((response) => {
      if (responseStatus === 200) {
        console.log(successCase);
        let firstPossibleDay = response['firstPossibleDay'];
        dispatch({type: successCase, firstPossibleDay: firstPossibleDay});
      } else if (responseStatus === 400 || responseStatus === 409 || responseStatus === 404) {
        return dispatchError(dispatchContext, response);
      } else {
        console.log(response);
        return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
      }
    }).catch(errorMessage => {
      console.log(errorMessage);
    });
  }
}
