import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createCopyTransactionModel} from "./transformTransactionModel";

export function updateTransaction(context, transactionModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(transactionModel, userId, transactionType));
  return function (dispatch) {
    return fetch(`/bm/transactions/update`, {
      method: 'PUT',
      headers: header,
      body: body
    }).then(function (response) {
        if (!response.ok) {
          throw Error(response.statusText);
        }
        return response.json();
      }
    ).then((response) => {
      let transactionModel = response['transactionModel'];
      console.log('UPDATE_' + transactionType + '_SUCCESS');
      console.log(response);
      dispatch({type: 'UPDATE_' + transactionType + '_SUCCESS', transactionModel: transactionModel, messages: messages});
    }).catch(err => {
      console.log('UPDATE_' + transactionType + '_ERROR');
      console.log(err);
      dispatch({type: 'UPDATE_' + transactionType + '_ERROR', messages: messages});
    });
  }
}

function createBody(transactionModel, userId, transactionType) {
  let body = {};
  body.userId = userId;
  body.transactionModel = createCopyTransactionModel(transactionModel, transactionModel.id);
  body.transactionType = transactionType;
  return body;
}
