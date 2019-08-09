const initState = {
  currenciesAreLoaded: false,
  currencies: []
}

const CurrencyReducer = (state = initState, action) => {
  switch (action.type) {
    case 'GET_CURRENCIES_SUCCESS':
      console.log('get currencies from server: success');
      return Object.assign({}, state, {
        currencies: action.currencies
      });
    case 'GET_CURRENCIES_ERROR':
      console.log('get currencies from server: error');
      return state;
    case 'CURRENCIES_ARE_READY':
      console.log('currencies are ready to use.');
      return {
        ...state,
        currenciesAreLoaded: true
      }
    default:
      return state;
  }
};

export default CurrencyReducer;