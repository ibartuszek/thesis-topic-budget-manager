export function fetchMainCategories(path) {
  return function (dispatch) {
    return fetch(`/bm/mainCategories/findAll`, {
      method: 'POST',
      body: JSON.stringify(path),
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(
      response => response.json(),
    ).then((incomeMainCategories) => {
      dispatch({type: 'GET_INCOME_MAIN_CATEGORIES_SUCCESS', incomeMainCategories});
      console.log(incomeMainCategories);
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
