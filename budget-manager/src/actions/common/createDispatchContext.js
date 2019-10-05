export function createDispatchContext(dispatch, messages, successCase, errorCase) {
  return {
    dispatch: dispatch,
    messages: messages,
    successCase: successCase,
    errorCase: errorCase
  }
}
