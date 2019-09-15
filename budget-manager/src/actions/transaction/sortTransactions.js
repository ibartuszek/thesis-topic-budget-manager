export function sortTransactions(field, transactions) {
  let newTransactions;
  if (field === 'mainCategory' || field === 'subCategory') {
    newTransactions = transactions.sort((a, b) =>
      (b[field] === null || (a[field] !== null && a[field].name.value > b[field].name.value)) ? 1
        : ((a[field] !== null && b[field].name.value > a[field].name.value) ? -1 : 0));
  } else if (field === 'monthly') {
    newTransactions = transactions.sort((a, b) =>
      (a[field] > b[field]) ? 1 : ((b[field] > a[field]) ? -1 : 0));
  } else {
    newTransactions = transactions.sort((a, b) =>
      (b[field] === null || (a[field] !== null && a[field].value > b[field].value)) ? 1
        : ((a[field] === null || b[field].value > a[field].value) ? -1 : 0));
  }
  return newTransactions;
}
