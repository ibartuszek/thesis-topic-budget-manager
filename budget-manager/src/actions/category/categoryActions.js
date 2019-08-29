export function fetchMainCategories(body) {
  return function (dispatch) {
    return fetch(`/bm/mainCategories/findAll`, {
      method: 'POST',
      body: JSON.stringify(body),
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(
      response => response.json(),
    ).then((mainCategories) => {
      dispatch({type: 'GET_INCOME_MAIN_CATEGORIES_SUCCESS', incomeMainCategories: mainCategories});
      console.log(mainCategories);
    }).catch(err => {
      dispatch({type: 'GET_INCOME_MAIN_CATEGORIES_ERROR', err});
    });
  }
}

export function setMainCategoriesToReady() {
  return (dispatch) => {
    dispatch({type: 'INCOME_MAIN_CATEGORIES_ARE_READY'});
  }
}
