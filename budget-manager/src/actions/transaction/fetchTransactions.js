import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {transformTransactionListFromResponse} from "./createTransactionMethods";
import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {dispatchError} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function fetchTransactions(context) {
  const {endDate, jwtToken, messages, startDate, transactionType, userId} = context;
  let url = '/bm/transactions/findAll?userId=' + userId
    + '&type=' + transactionType
    + '&start=' + startDate;
  url = endDate !== null ? url + '&end=' + endDate : url;

  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let successCase = 'GET_' + transactionType.toUpperCase() + 'S_SUCCESS';
  let errorCase = 'GET_' + transactionType.toUpperCase() + 'S_ERROR';
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
        let transactionList = response['transactionList'];
        dispatch({
          type: successCase,
          transactions: transformTransactionListFromResponse(transactionList)
        });
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
