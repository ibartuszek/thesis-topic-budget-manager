import fetch from 'cross-fetch';

export function fetchCurrencies(path) {
  return function(dispatch) {
    return fetch(`/currencies/getCurrencies`)
    .then(
      response => response.json(),
    ).then((currencies) => {
      dispatch({ type: 'GET_CURRENCIES_SUCCESS', currencies});
    }).catch(err => {
      dispatch({ type: 'GET_CURRENCIES_ERROR', err});
    });
  }
}

export function setCurrenciesToReady() {
  return (dispatch) => {
    dispatch({ type: 'CURRENCIES_ARE_READY'});
  }
}