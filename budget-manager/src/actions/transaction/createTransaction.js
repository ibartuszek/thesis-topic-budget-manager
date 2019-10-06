import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {transformTransactionFromResponse, transformTransactionToRequest} from "./createTransactionMethods";
import {defaultMessages} from "../../store/MessageHolder";

export function createTransaction(context, transactionModel, coordinate) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(transactionModel, userId, transactionType, coordinate));
  console.log(body);
  let successCase = 'CREATE_' + transactionType + '_SUCCESS';
  let errorCase = 'CREATE_' + transactionType + '_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(`/bm/transactions/create`, {
      method: 'POST',
      headers: header,
      body: body
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let transactionModel = transformTransactionFromResponse(responseBody['transaction']);
        return dispatchSuccess(dispatchContext, responseBody['message'], 'transactionModel', transactionModel);
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

function createBody(transactionModel, userId, transactionType, coordinate) {
  let body = {};
  body.userId = userId;
  body.transaction = transformTransactionToRequest(transactionModel, coordinate);
  body.transactionType = transactionType;
  return body;
}
