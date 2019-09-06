export function createContext(userHolder, logHolder) {
  return {
    userId: userHolder.userData.userId,
    jwtToken: userHolder.jwtToken,
    messages: logHolder.messages
  };
}
