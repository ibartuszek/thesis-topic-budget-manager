export function addMessage(messages, message) {
  let found = false;
  for (let key in messages) {
    let m = messages[key];
    if (m.key === message.key && m.value === message.value) {
      found = true;
      break;
    }
  }
  if (!found) {
    messages.push(message);
  }
  return function (dispatch) {
    dispatch({type: 'ADD_MESSAGE', messages: messages});
  }
}

export function removeMessage(messages, message) {
  for (let index = 0; index < messages.length; index++) {
    if (messages[index].key === message.key) {
      messages.splice(index, 1);
      break;
    }
  }
  return function (dispatch) {
    dispatch({type: 'REMOVE_MESSAGE', messages: messages});
  }
}

export function getMessage(messages, messageKey, success) {
  let result = null;
  for (let index = 0; index < messages.length && result === null; index++) {
    if (messages[index].key === messageKey) {
      result = messages[index].value;
    }
  }
  return {
    key: messageKey,
    value: result,
    success: success
  };
}

export function createMessage(key, message, success) {
  return {
    key: key,
    value: message,
    success: success
  };
}
