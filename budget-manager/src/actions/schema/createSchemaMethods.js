import {transformMainCategoryFromResponse, transformMainCategoryToRequest} from "../category/createMainCategoryMethods";
import {transformSubCategoryToRequest} from "../category/createSubCategoryMethods";

export function createEmptySchema() {
  return {
    id: null,
    title: {
      value: '',
      errorMessage: null,
      minimumLength: 2,
      maximumLength: 20,
    },
    type: {
      value: "SCALE",
      errorMessage: null,
      possibleEnumValues: [
        "SCALE",
        "SUM"
      ]
    },
    currency: {
      value: "HUF",
      errorMessage: null,
      possibleEnumValues: [
        "EUR",
        "USD",
        "HUF"
      ]
    },
    chartType: {
      value: "RADIAL",
      errorMessage: null,
      possibleEnumValues: [
        "RADIAL",
        "BAR",
        "LINEAR"
      ]
    },
    mainCategory: undefined,
    subCategory: undefined
  };
}

export function transformSchemaFromResponse(responseModel) {
  let schema = createEmptySchema();
  schema.id = responseModel['id'];
  schema.title.value = responseModel['title'];
  schema.type.value = responseModel['type'];
  schema.currency.value = responseModel['currency'];
  schema.chartType.value = responseModel['chartType'];

  let mainCategory = responseModel['mainCategory'];
  if (mainCategory !== undefined && mainCategory !== null) {
    schema.mainCategory = transformMainCategoryFromResponse(mainCategory);
  }

  let subCategory = responseModel['subCategory'];
  if (subCategory !== undefined && subCategory !== null) {
    schema.subCategory = transformMainCategoryFromResponse(subCategory);
  }

  return schema;
}

export function transformSchemaListFromResponse(responseModelList) {
  return responseModelList.map(transformSchemaFromResponse);
}

export function transformSchemaToRequest(schemaModel) {
  let mainCategory = null;
  if (schemaModel.mainCategory !== undefined && schemaModel.mainCategory !== null) {
    mainCategory = transformMainCategoryToRequest(schemaModel.mainCategory)
  }
  let subCategory = null;
  if (schemaModel.subCategory !== undefined && schemaModel.subCategory !== null) {
    subCategory = transformSubCategoryToRequest(schemaModel.subCategory)
  }
  return {
    id: schemaModel.id,
    title: schemaModel.title.value,
    type: schemaModel.type.value,
    currency: schemaModel.currency.value,
    chartType: schemaModel.chartType.value,
    mainCategory: mainCategory,
    subCategory: subCategory,
  };
}
