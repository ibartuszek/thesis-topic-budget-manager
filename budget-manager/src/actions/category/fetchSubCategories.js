import {createHeaderWithJwt} from "../common/createHeader";

export function fetchSubCategories(context, type) {
  const {userId, jwtToken, messages} = context;
  let url = '/bm/subCategories/findAll?userId=' + userId + '&type=' + type;
  let header = createHeaderWithJwt(jwtToken);
  let successCase = 'GET_' + type.toUpperCase() + '_SUB_CATEGORIES_SUCCESS';
  let errorCase = 'GET_' + type.toUpperCase() + '_SUB_CATEGORIES_ERROR';

  return function (dispatch) {
    return fetch(url, {
      method: 'GET',
      headers: header
    }).then(function (response) {
      if (!response.ok) {
        throw Error(response.statusText);
      }
      return response.json();
    }).then((subCategories) => {
      console.log(successCase);
      dispatch({type: successCase, subCategories: subCategories});
    }).catch(err => {
      console.log(errorCase);
      console.log(err);
      dispatch({type: errorCase, messages});
    });
  }
}
