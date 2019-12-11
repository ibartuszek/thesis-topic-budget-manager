export function createCardNames(typeData) {
  const {transactionType, typeWithCapitalized, typeLowerCase} = typeData;
  const createMainType = "create" + typeWithCapitalized + "MainType";
  const createMainTypeButtonName = "Create new main type";
  const createSubType = "create" + typeWithCapitalized + "SubType";
  const createSubTypeButtonName = "Create new subtype";
  const createTransaction = "create" + typeWithCapitalized;
  const createTransactionButtonName = "Create " + typeLowerCase;
  const mainCategorySetName = typeLowerCase + "MainCategories";
  const subCategorySetName = typeLowerCase + "SubCategories";

  return {
    createTransactionButtonName: createTransactionButtonName,
    createTransaction: createTransaction,
    createMainTypeButtonName: createMainTypeButtonName,
    createMainType: createMainType,
    createSubTypeButtonName: createSubTypeButtonName,
    createSubType: createSubType,
    mainCategorySetName: mainCategorySetName,
    subCategorySetName: subCategorySetName,
    transactionType: transactionType
  };
}
