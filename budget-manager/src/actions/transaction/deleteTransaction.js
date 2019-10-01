import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {transformTransactionFromResponse, transformTransactionToRequest} from "./createTransactionMethods";

export function deleteTransaction(context, transactionModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(transactionModel, userId, transactionType));
  return function (dispatch) {
    return fetch(`/bm/transactions/delete`, {
      method: 'DELETE',
      headers: header,
      body: body
    }).then(function (response) {
        if (!response.ok) {
          throw Error(response.statusText);
        }
        return response.json();
      }
    ).then((response) => {
      let transaction = response['transaction'];
      console.log('DELETE_' + transactionType + '_SUCCESS');
      console.log(response);
      dispatch({
        type: 'DELETE_' + transactionType + '_SUCCESS',
        transactionModel: transformTransactionFromResponse(transaction), messages: messages
      });
    }).catch(err => {
      console.log('DELETE_' + transactionType + '_ERROR');
      console.log(err);
      dispatch({type: 'DELETE_' + transactionType + '_ERROR', messages: messages});
    });
  }
}

function createBody(transactionModel, userId, transactionType) {
  let body = {};
  body.userId = userId;
  body.transaction = transformTransactionToRequest(transactionModel);
  body.transactionType = transactionType;
  return body;
}
