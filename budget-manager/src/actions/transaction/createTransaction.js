import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createCopyTransactionModel} from "./transformTransactionModel";

export function createTransaction(context, transactionModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(transactionModel, userId, transactionType));

  console.log(header);
  console.log(body);

  return function (dispatch) {
    return fetch(`/bm/transactions/create`, {
      method: 'POST',
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
      console.log('CREATE_' + transactionType + '_SUCCESS');
      console.log(response);
      dispatch({type: 'CREATE_' + transactionType + '_SUCCESS', transactionModel: transactionModel, messages: messages});
    }).catch(err => {
      console.log('CREATE_' + transactionType + '_ERROR');
      console.log(err);
      dispatch({type: 'CREATE_' + transactionType + '_ERROR', messages: messages});
    });
  }
}

function createBody(transactionModel, userId, transactionType) {
  let body = {};
  body.userId = userId;
  body.transactionModel = createCopyTransactionModel(transactionModel);
  body.transactionType = transactionType;
  return body;
}
