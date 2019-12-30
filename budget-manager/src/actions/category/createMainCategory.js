import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {transformMainCategoryFromResponse, transformMainCategoryToRequest} from "./createMainCategoryMethods";
import {defaultMessages} from "../../store/MessageHolder";

export function createMainCategory(context, mainCategoryModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(mainCategoryModel, userId, transactionType));
  let successCase = 'CREATE_' + transactionType + '_MAIN_CATEGORY_SUCCESS';
  let errorCase = 'CREATE_' + transactionType + '_MAIN_CATEGORY_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(process.env.REACT_APP_API_ENDPOINT + `/bm/mainCategories/create`, {
      method: 'POST',
      headers: header,
      body: body
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let mainCategoryModel = transformMainCategoryFromResponse(responseBody['mainCategory']);
        return dispatchSuccess(dispatchContext, responseBody['message'], 'mainCategoryModel', mainCategoryModel);
      } else if (responseStatus === 400 || responseStatus === 409 || responseStatus === 404) {
        return dispatchError(dispatchContext, responseBody);
      } else {
        return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
      }
    }).catch(errorMessage => {
      console.log(errorMessage);
    });
  }
}

function createBody(mainCategoryModel, userId, transactionType) {
  let body = {};
  body.userId = userId;
  body.mainCategory = transformMainCategoryToRequest(mainCategoryModel);
  body.transactionType = transactionType;
  return body;
}
