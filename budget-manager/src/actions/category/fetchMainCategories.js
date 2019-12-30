import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwt} from "../common/createHeader";
import {dispatchError} from "../common/dispatchActions";
import {transformMainCategoryListFromResponse} from "./createMainCategoryMethods";
import {defaultMessages} from "../../store/MessageHolder";

export function fetchMainCategories(context, type) {
  const {userId, jwtToken, messages} = context;
  let url = process.env.REACT_APP_API_ENDPOINT
    + '/bm/mainCategories/findAll?userId=' + userId
    + '&type=' + type;
  let header = createHeaderWithJwt(jwtToken);
  let successCase = 'GET_' + type.toUpperCase() + '_MAIN_CATEGORIES_SUCCESS';
  let errorCase = 'GET_' + type.toUpperCase() + '_MAIN_CATEGORIES_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(url, {
      method: 'GET',
      headers: header
    }).then(function (response) {
      responseStatus = response.status;
      return responseStatus === 200 ? response.json() : createErrorBody(response);
    }).then((response) => {
      if (responseStatus === 200) {
        let mainCategoryList = response['mainCategoryList'];
        console.log(successCase);
        dispatch({
          type: successCase,
          mainCategories: transformMainCategoryListFromResponse(mainCategoryList)
        });
      } else if (responseStatus === 400 || responseStatus === 409 || responseStatus === 404) {
        return dispatchError(dispatchContext, response);
      } else {
        console.log(response);
        return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
      }
    }).catch(errorMessage => {
      console.log(errorMessage);
    });
  }
}

