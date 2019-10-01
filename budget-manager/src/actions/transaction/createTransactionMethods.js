import {getPossibleFirstDate} from "../date/dateActions";
import {transformMainCategoryFromResponse, transformMainCategoryToRequest} from "../category/createMainCategoryMethods";
import {transformSubCategoryToRequest} from "../category/createSubCategoryMethods";

export function createEmptyTransaction() {
  let possibleFirstDay = getPossibleFirstDate();
  return {
    id: null,
    title: {
      value: '',
      errorMessage: null,
      minimumLength: 2,
      maximumLength: 50,
    },
    amount: {
      value: 1.0,
      errorMessage: null,
      positive: true,
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
    transactionType: {
      value: null,
      errorMessage: null,
      possibleEnumValues: [
        "INCOME",
        "OUTCOME"
      ]
    },
    mainCategory: undefined,
    subCategory: undefined,
    monthly: false,
    date: {
      value: null,
      errorMessage: null,
      possibleFirstDay: possibleFirstDay
    },
    endDate: {
      value: null,
      errorMessage: null,
      possibleFirstDay: possibleFirstDay
    },
    description: {
      value: '',
      errorMessage: null,
      minimumLength: 2,
      maximumLength: 100,
    },
    locked: false
  };
}

export function transformTransactionFromResponse(responseModel) {
  let transaction = createEmptyTransaction();
  transaction.id = responseModel['id'];
  transaction.title.value = responseModel['title'];
  transaction.amount.value = responseModel['amount'];
  transaction.currency.value = responseModel['currency'];
  transaction.transactionType.value = responseModel['transactionType'];
  transaction.mainCategory = transformMainCategoryFromResponse(responseModel['mainCategory']);
  transaction.date.value = responseModel['date'];
  transaction.monthly = responseModel['monthly'];
  transaction.locked = responseModel['locked'];

  let subCategory = responseModel['subCategory'];
  if (subCategory !== undefined && subCategory !== null) {
    transaction.subCategory = transformMainCategoryFromResponse(subCategory);
  }

  let endDate = responseModel['endDate'];
  if (endDate !== undefined && endDate !== null) {
    transaction.endDate.value = endDate;
  } else if (endDate !== null) {
    transaction.endDate.value = '';
  }

  let description = responseModel['description'];
  if (description !== undefined && description !== null) {
    transaction.description.value = description;
  }

  return transaction;
}

export function transformTransactionListFromResponse(responseModelList) {
  return responseModelList.map(transformTransactionFromResponse);
}

export function transformTransactionToRequest(transactionModel) {
  let subCategory = null;
  if (transactionModel.subCategory !== undefined && transactionModel.subCategory !== null) {
    subCategory = transformSubCategoryToRequest(transactionModel.subCategory)
  }
  return {
    id: transactionModel.id,
    title: transactionModel.title.value,
    amount: transactionModel.amount.value,
    currency: transactionModel.currency.value,
    transactionType: transactionModel.transactionType.value,
    mainCategory: transformMainCategoryToRequest(transactionModel.mainCategory),
    subCategory: subCategory,
    monthly: transactionModel.monthly,
    date: transactionModel.date.value,
    endDate: replaceOptionalField(transactionModel, 'endDate'),
    description: replaceOptionalField(transactionModel, 'description'),
    locked: transactionModel.locked
  };
}

function replaceOptionalField(model, optionalFieldName) {
  return model[optionalFieldName] !== undefined
  && model[optionalFieldName] !== undefined
  && model[optionalFieldName].value !== null
  && model[optionalFieldName].value !== ''
    ? model[optionalFieldName].value
    : null;
}
