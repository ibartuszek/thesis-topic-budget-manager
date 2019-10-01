export function createEmptySubCategory() {
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
    }
  };
}

export function transformSubCategoryFromResponse(responseModel) {
  let subCategory = createEmptySubCategory();
  subCategory.id = responseModel['id'];
  subCategory.name.value = responseModel['name'];
  subCategory.transactionType.value = responseModel['transactionType'];
  return subCategory;
}

export function transformSubCategoryListFromResponse(responseModelList) {
  return responseModelList.map(transformSubCategoryFromResponse);
}

export function transformSubCategoryToRequest(subCategoryModel) {
  return {
    id: subCategoryModel.id,
    name: subCategoryModel.name.value,
    transactionType: subCategoryModel.transactionType.value
  };
}

export function transformSubCategoryListToRequest(subCategoryModelList) {
  return subCategoryModelList.map(transformSubCategoryToRequest);
}
