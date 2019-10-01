import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {transformSubCategoryFromResponse, transformSubCategoryToRequest} from "./createSubCategoryMethods";

export function createSubCategory(context, subCategoryModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(subCategoryModel, userId, transactionType));

  return function (dispatch) {
    return fetch(`/bm/subCategories/create`, {
      method: 'POST',
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
      console.log('CREATE_' + transactionType + '_SUB_CATEGORY_SUCCESS');
      console.log(response);
      dispatch({
        type: 'CREATE_' + transactionType + '_SUB_CATEGORY_SUCCESS',
        subCategoryModel: transformSubCategoryFromResponse(subCategory), messages: messages
      });
    }).catch(err => {
      console.log('CREATE_' + transactionType + '_SUB_CATEGORY_ERROR');
      console.log(err);
      dispatch({type: 'CREATE_' + transactionType + '_SUB_CATEGORY_ERROR', messages: messages});
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
