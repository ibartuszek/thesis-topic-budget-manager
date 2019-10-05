import {transformSubCategoryListFromResponse, transformSubCategoryListToRequest} from "./createSubCategoryMethods";

export function createEmptyMainCategory() {
  return {
    id: null,
    name: {
      value: '',
      errorMessage: null,
      minimumLength: 2,
      maximumLength: 50,
    },
    transactionType: {
      value: null,
      errorMessage: null,
      possibleEnumValues: [
        "INCOME",
        "OUTCOME"
      ]
    },
    subCategoryModelSet: []
  };
}

export function transformMainCategoryFromResponse(responseModel) {
  let subCategoryModelSet = [];
  if (responseModel['subCategorySet'] !== undefined) {
    subCategoryModelSet = transformSubCategoryListFromResponse(responseModel['subCategorySet'])
  }
  let mainCategory = createEmptyMainCategory();
  mainCategory.id = responseModel['id'];
  mainCategory.name.value = responseModel['name'];
  mainCategory.transactionType.value = responseModel['transactionType'];
  mainCategory.subCategoryModelSet = subCategoryModelSet;
  return mainCategory;
}

export function transformMainCategoryListFromResponse(responseModelList) {
  return responseModelList.map(transformMainCategoryFromResponse);
}

export function transformMainCategoryToRequest(mainCategoryModel) {
  return {
    id: mainCategoryModel.id,
    name: mainCategoryModel.name.value,
    transactionType: mainCategoryModel.transactionType.value,
    subCategorySet: transformSubCategoryListToRequest(mainCategoryModel.subCategoryModelSet)
  };
}
