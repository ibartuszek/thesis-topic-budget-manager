import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";

export function fetchTransactions(context) {
  const {endDate, jwtToken, messages, startDate, transactionType, userId} = context;
  let url = '/bm/transactions/findAll?userId=' + userId
    + '&type=' + transactionType
    + '&start=' + startDate;
  url = endDate !== null ? url + '&end=' + endDate : url;

  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let successCase = 'GET_' + transactionType.toUpperCase() + 'S_SUCCESS';
  let errorCase = 'GET_' + transactionType.toUpperCase() + 'S_ERROR';

  return function (dispatch) {
    return fetch(url, {
      method: 'GET',
      headers: header
    }).then(function (response) {
      if (!response.ok) {
        throw Error(response.statusText);
      }
      return response.json();
    }).then((transactions) => {
      console.log(successCase);
      dispatch({type: successCase, transactions: transactions});
    }).catch(err => {
      console.log(errorCase);
      console.log(err);
      dispatch({type: errorCase, messages});
    });
  }
}
