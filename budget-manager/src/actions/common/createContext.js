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
