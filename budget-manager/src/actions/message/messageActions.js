import moment from "moment";

export function addMessage(messages, message) {
  for (let index = 0; index < messages.length; index++) {
    if (messages[index].key === message.key && messages[index].show === true) {
      messages[index].show = false;
      break;
    }
  }
  messages.push(message);
  return function (dispatch) {
    dispatch({type: 'ADD_MESSAGE', messages: messages});
  }
}

export function removeMessage(messages, message) {
  let found = false;
  for (let index = 0; index < messages.length; index++) {
    if (messages[index].key === message.key && messages[index].show === true) {
      messages[index].show = false;
      found = true;
      break;
    }
  }
  if (found) {
    return function (dispatch) {
      dispatch({type: 'REMOVE_MESSAGE', messages: messages});
    }
  }
}

export function getMessage(messages, messageKey, success) {
  let result = null;
  for (let index = 0; index < messages.length && result === null; index++) {
    if (messages[index].key === messageKey && messages[index].show === true) {
      result = messages[index];
    }
  }
  let emptyMessage = {
    key: messageKey,
    value: null,
    success: success
  };
  return result !== null ? result : emptyMessage;
}

export function createMessage(key, message, success) {
  return {
    date: moment().format("YYYY-MM-DD HH:mm"),
    key: key,
    value: message,
    success: success,
    show: true
  };
}
