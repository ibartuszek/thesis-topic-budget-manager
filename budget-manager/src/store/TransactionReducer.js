import {addMessage, createMessage} from "../actions/message/messageActions";
import {transactionMessages} from "./MessageHolder"
import {addElementToArray, removeElementFromArray, replaceElementAtArray} from "../actions/common/listActions";

const initState = {
  incomesAreLoaded: false,
  incomes: [],
};

const TransactionReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'CREATE_INCOME_SUCCESS':
      key = "createTransactionSuccess";
      addMessage(action.messages, createMessage(key, true, transactionMessages));
      return Object.assign({}, state, {
        ...state,
        incomes: addElementToArray(state.incomes, action.transactionModel),
      });
    case 'CREATE_INCOME_ERROR':
      key = "createTransactionError";
      addMessage(action.messages, createMessage(key, false, transactionMessages));
      return Object.assign({}, state, {
        ...state,
      });
    case 'DELETE_INCOME_SUCCESS':
      key = "deleteTransactionSuccess";
      addMessage(action.messages, createMessage(key, true, transactionMessages));
      return Object.assign({}, state, {
        ...state,
        incomes: removeElementFromArray(state.incomes, action.transactionModel),
      });
    case 'DELETE_INCOME_ERROR':
      key = "deleteTransactionError";
      addMessage(action.messages, createMessage(key, true, transactionMessages));
      return Object.assign({}, state, {
        ...state,
      });
    case 'GET_INCOMES_SUCCESS':
      return Object.assign({}, state, {
        ...state,
        incomes: action.transactions,
      });
    case 'GET_INCOMES_ERROR':
      key = "transactionsUnavailableMessage";
      addMessage(action.messages, createMessage(key, false, transactionMessages));
      return {
        ...state,
      };
    case 'INCOMES_ARE_READY':
      return {
        ...state,
        incomesAreLoaded: true,
      };
    case 'UPDATE_INCOME_SUCCESS':
      key = "updateTransactionSuccess";
      addMessage(action.messages, createMessage(key, true, transactionMessages));
      return Object.assign({}, state, {
        ...state,
        incomes: replaceElementAtArray(state.incomes, action.transactionModel),
      });
    case 'UPDATE_INCOME_ERROR':
      key = "updateTransactionError";
      addMessage(action.messages, createMessage(key, true, transactionMessages));
      return Object.assign({}, state, {
        ...state,
      });
    default:
      return state;
  }
};

export default TransactionReducer;
