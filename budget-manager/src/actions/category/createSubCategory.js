import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {transformSubCategoryFromResponse, transformSubCategoryToRequest} from "./createSubCategoryMethods";
import {defaultMessages} from "../../store/MessageHolder";

export function createSubCategory(context, subCategoryModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(subCategoryModel, userId, transactionType));
  let successCase = 'CREATE_' + transactionType + '_SUB_CATEGORY_SUCCESS';
  let errorCase = 'CREATE_' + transactionType + '_SUB_CATEGORY_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(`/bm/subCategories/create`, {
      method: 'POST',
      headers: header,
      body: body
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let subCategoryModel = transformSubCategoryFromResponse(responseBody['subCategory']);
        return dispatchSuccess(dispatchContext, responseBody['message'], 'subCategoryModel', subCategoryModel);
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

function createBody(subCategoryModel, userId, transactionType) {
  let body = {};
  body.userId = userId;
  body.subCategory = transformSubCategoryToRequest(subCategoryModel);
  body.transactionType = transactionType;
  return body;
}
