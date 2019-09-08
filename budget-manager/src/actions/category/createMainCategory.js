import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";

export function createMainCategory(context, mainCategoryModel) {
  const {userId, jwtToken, messages, transactionType} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(mainCategoryModel, userId, transactionType));

  return function (dispatch) {
    return fetch(`/bm/mainCategories/create`, {
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
      let mainCategoryModel = response['mainCategoryModel'];
      console.log('CREATE_' + transactionType + '_MAIN_CATEGORY_SUCCESS');
      console.log(response);
      dispatch({type: 'CREATE_' + transactionType + '_MAIN_CATEGORY_SUCCESS', mainCategoryModel: mainCategoryModel, messages: messages});
    }).catch(err => {
      console.log('CREATE_' + transactionType + '_MAIN_CATEGORY_ERROR');
      console.log(err);
      dispatch({type: 'CREATE_' + transactionType + '_MAIN_CATEGORY_ERROR', messages: messages});
    });
  }
}

function createBody(mainCategoryModel, userId, transactionType) {
  let body = {};
  body.userId = userId;
  body.mainCategoryModel = mainCategoryModel;
  body.transactionType = transactionType;
  return body;
}
