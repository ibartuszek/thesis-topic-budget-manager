import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {dispatchError} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function lockTransactions(context, endDate) {
  const {jwtToken, messages, userId} = context;
  let url = process.env.REACT_APP_API_ENDPOINT + '/bm/transactions/lockTransactions?userId=' + userId + '&end=' + endDate;

  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let successCase = 'TRANSACTIONS_LOCKED_SUCCESS';
  let errorCase = 'TRANSACTIONS_LOCKED_ERROR';
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
        dispatch({type: successCase, message: response['message'], messages: messages});
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
