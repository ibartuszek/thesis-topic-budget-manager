export function createContext(userHolder, logHolder) {
  return {
    userId: userHolder.userData.userId,
    jwtToken: userHolder.jwtToken,
    messages: logHolder.messages
  };
}

export function createTransactionContext(userHolder, logHolder, transactionType) {
  return {
    userId: userHolder.userData.userId,
    jwtToken: userHolder.jwtToken,
    messages: logHolder.messages,
    transactionType: transactionType
  };
}

export function createFetchTransactionsContext(userHolder, logHolder, parameters) {
  return {
    userId: userHolder.userData.userId,
    jwtToken: userHolder.jwtToken,
    messages: logHolder.messages,
    transactionType: parameters['transactionType'],
    endDate: parameters['endDate'],
    startDate: parameters['startDate'],
  }
}

export function createContextParameters(endDate, startDate, transactionType) {
  let eDate = endDate !== undefined ? endDate : null;
  return {
    transactionType: transactionType,
    startDate: startDate,
    endDate: eDate
  };
}
