import {addMessage, createMessage} from "../actions/message/messageActions";
import {addElementToArray, removeElementFromArray, replaceElementAtArray} from "../actions/common/listActions";

const initState = {
  incomesAreLoaded: false,
  incomes: [],
  outcomesAreLoaded: false,
  outcomes: [],
  firstPossibleDay: null
};

const TransactionReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'CREATE_INCOME_SUCCESS':
      key = "createTransactionSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        incomes: addElementToArray(state.incomes, action.transactionModel),
      });
    case 'CREATE_INCOME_ERROR':
      key = "createTransactionError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
      });
    case 'DELETE_INCOME_SUCCESS':
      key = "deleteTransactionSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        incomes: removeElementFromArray(state.incomes, action.transactionModel),
      });
    case 'DELETE_INCOME_ERROR':
      key = "deleteTransactionError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
      });
    case 'GET_INCOMES_SUCCESS':
      return Object.assign({}, state, {
        ...state,
        incomes: action.transactions,
        incomesAreLoaded: true,
      });
    case 'GET_INCOMES_ERROR':
      key = "transactionsUnavailableMessage";
      addMessage(action.messages, createMessage(key, action.message, false));
      return {
        ...state,
      };
    case 'UPDATE_INCOME_SUCCESS':
      key = "updateTransactionSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        incomes: replaceElementAtArray(state.incomes, action.transactionModel),
      });
    case 'UPDATE_INCOME_ERROR':
      key = "updateTransactionError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
      });

    case 'CREATE_OUTCOME_SUCCESS':
      key = "createTransactionSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        outcomes: addElementToArray(state.outcomes, action.transactionModel),
      });
    case 'CREATE_OUTCOME_ERROR':
      key = "createTransactionError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
      });
    case 'DELETE_OUTCOME_SUCCESS':
      key = "deleteTransactionSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        outcomes: removeElementFromArray(state.outcomes, action.transactionModel),
      });
    case 'DELETE_OUTCOME_ERROR':
      key = "deleteTransactionError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
      });
    case 'GET_OUTCOMES_SUCCESS':
      return Object.assign({}, state, {
        ...state,
        outcomes: action.transactions,
        outcomesAreLoaded: true,
      });
    case 'GET_OUTCOMES_ERROR':
      key = "transactionsUnavailableMessage";
      addMessage(action.messages, createMessage(key, action.message, false));
      return {
        ...state,
      };
    case 'UPDATE_OUTCOME_SUCCESS':
      key = "updateTransactionSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        outcomes: replaceElementAtArray(state.outcomes, action.transactionModel),
      });
    case 'UPDATE_OUTCOME_ERROR':
      key = "updateTransactionError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
      });
    case 'GET_FIRST_POSSIBLE_DAY_SUCCESS':
      return Object.assign({}, state, {
        ...state,
        firstPossibleDay: action.firstPossibleDay,
      });
    case 'GET_FIRST_POSSIBLE_DAY_ERROR':
      key = "getFirstPossibleDayError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
      });
    case 'TRANSACTIONS_LOCKED_SUCCESS':
      key = "transactionsLockedSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        incomesAreLoaded: false,
        incomes: [],
        outcomesAreLoaded: false,
        outcomes: [],
        firstPossibleDay: null
      });
    case 'TRANSACTIONS_LOCKED_ERROR':
      key = "transactionsLockedError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state,
        incomesAreLoaded: false,
        incomes: [],
        outcomesAreLoaded: false,
        outcomes: [],
        firstPossibleDay: null
      });

    default:
      return state;
  }
};

export default TransactionReducer;
