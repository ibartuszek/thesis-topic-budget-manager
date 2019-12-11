import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwt} from "../common/createHeader";
import {dispatchError} from "../common/dispatchActions";
import {transformSubCategoryListFromResponse} from "./createSubCategoryMethods";
import {defaultMessages} from "../../store/MessageHolder";

export function fetchSubCategories(context, type) {
  const {userId, jwtToken, messages} = context;
  let url = '/bm/subCategories/findAll?userId=' + userId + '&type=' + type;
  let header = createHeaderWithJwt(jwtToken);
  let successCase = 'GET_' + type.toUpperCase() + '_SUB_CATEGORIES_SUCCESS';
  let errorCase = 'GET_' + type.toUpperCase() + '_SUB_CATEGORIES_ERROR';
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
        let subCategoryList = response['subCategoryList'];
        console.log(successCase);
        dispatch({
          type: successCase,
          subCategories: transformSubCategoryListFromResponse(subCategoryList)
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
