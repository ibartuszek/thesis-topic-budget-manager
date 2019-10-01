import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {transformSubCategoryFromResponse, transformSubCategoryToRequest} from "./createSubCategoryMethods";

export function updateSubCategory(context, subCategoryModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(subCategoryModel, userId, transactionType));
  return function (dispatch) {
    return fetch(`/bm/subCategories/update`, {
      method: 'PUT',
      headers: header,
      body: body
    }).then(function (response) {
        if (!response.ok) {
          throw Error(response.statusText);
        }
        return response.json();
      }
    ).then((response) => {
      let subCategory = response['subCategory'];
      console.log('UPDATE_' + transactionType + '_SUB_CATEGORY_SUCCESS');
      console.log(response);
      dispatch({
        type: 'UPDATE_' + transactionType + '_SUB_CATEGORY_SUCCESS',
        subCategoryModel: transformSubCategoryFromResponse(subCategory), messages: messages
      });
    }).catch(err => {
      console.log('UPDATE_' + transactionType + '_SUB_CATEGORY_ERROR');
      console.log(err);
      dispatch({type: 'UPDATE_' + transactionType + '_SUB_CATEGORY_ERROR', messages: messages});
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
