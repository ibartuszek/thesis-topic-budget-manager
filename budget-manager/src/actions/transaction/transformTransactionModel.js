import {getPossibleFirstDate} from "../date/dateActions";

export function transformTransactionModel(transactionFromRepo) {
  let possibleFirstDay = getPossibleFirstDate();
  return {
    id: transactionFromRepo.id,
    title: {
      value: transactionFromRepo.title.value,
      errorMessage: null,
      minimumLength: 2,
      maximumLength: 50,
    },
    amount: {
      value: transactionFromRepo.amount.value,
      errorMessage: null,
      positive: true,
    },
    currency: {
      value: transactionFromRepo.currency.value,
      errorMessage: null,
      possibleEnumValues: [
        "EUR",
        "USD",
        "HUF"
      ]
    },
    transactionType: {
      value: transactionFromRepo.transactionType.value,
      errorMessage: null,
      possibleEnumValues: [
        "INCOME",
        "OUTCOME"
      ]
    },
    mainCategory: transactionFromRepo.mainCategory,
    subCategory: transactionFromRepo.subCategory,
    monthly: transactionFromRepo.monthly,
    date: {
      value: transactionFromRepo.date.value,
      possibleFirstDay: possibleFirstDay
    },
    endDate: {
      value: transactionFromRepo.endDate !== null ? transactionFromRepo.endDate.value : '',
      possibleFirstDay: possibleFirstDay
    },
    description: {
      value: transactionFromRepo.description !== null ? transactionFromRepo.description.value : '',
      errorMessage: null,
      minimumLength: 2,
      maximumLength: 100,
    },
    locked: transactionFromRepo.locked
  };
}

export function createCopyTransactionModel(transactionModel, id) {
  return {
    id: id !== undefined ? id : null,
    title: {
      value: transactionModel.title.value
    },
    amount: {
      value: transactionModel.amount.value
    },
    currency: {
      value: transactionModel.currency.value
    },
    transactionType: {
      value: transactionModel.transactionType.value
    },
    mainCategory: transactionModel.mainCategory,
    subCategory: transactionModel.subCategory,
    monthly: transactionModel.monthly,
    date: {
      value: transactionModel.date.value
    },
    endDate: replaceOptionalField(transactionModel, 'endDate'),
    description: replaceOptionalField(transactionModel, 'description'),
    locked: false
  };
}

function replaceOptionalField(model, optionalFieldName) {
  return model[optionalFieldName] !== undefined
  && model[optionalFieldName] !== undefined
  && model[optionalFieldName].value !== null
  && model[optionalFieldName].value !== ''
    ? model[optionalFieldName]
    : null;
}
