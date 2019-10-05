export function dispatchSuccess(dispatchContext, message, targetName, target) {
  console.log(dispatchContext.successCase);
  return dispatchContext.dispatch({
    type: dispatchContext.successCase,
    [targetName]: target,
    message: message,
    messages: dispatchContext.messages
  });
}

export function dispatchError(dispatchContext, message) {
  console.log(dispatchContext.errorCase);
  return dispatchContext.dispatch({
    type: dispatchContext.errorCase,
    message: message,
    messages: dispatchContext.messages
  });
}
