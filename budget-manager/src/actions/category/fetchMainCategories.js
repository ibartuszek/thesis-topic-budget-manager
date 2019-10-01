import {createHeaderWithJwt} from "../common/createHeader";
import {transformMainCategoryListFromResponse} from "./createMainCategoryMethods";

export function fetchMainCategories(context, type) {
  const {userId, jwtToken, messages} = context;
  let url = '/bm/mainCategories/findAll?userId=' + userId + '&type=' + type;
  let header = createHeaderWithJwt(jwtToken);
  let successCase = 'GET_' + type.toUpperCase() + '_MAIN_CATEGORIES_SUCCESS';
  let errorCase = 'GET_' + type.toUpperCase() + '_MAIN_CATEGORIES_ERROR';

  return function (dispatch) {
    return fetch(url, {
      method: 'GET',
      headers: header
    }).then(function (response) {
      if (!response.ok) {
        throw Error(response.statusText);
      }
      return response.json();
    }).then((response) => {
      let mainCategoryList = response['mainCategoryList'];
      console.log(successCase);
      dispatch({type: successCase, mainCategories: transformMainCategoryListFromResponse(mainCategoryList)});
    }).catch(err => {
      console.log(errorCase);
      console.log(err);
      dispatch({type: errorCase, messages});
    });
  }
}
