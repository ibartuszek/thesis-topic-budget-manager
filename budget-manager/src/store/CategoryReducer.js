import {addMessage, createMessage} from "../actions/message/messageActions";

const initState = {
  incomeMainCategoriesAreLoaded: false,
  incomeSubCategoriesAreLoaded: false,
  incomeMainCategories: [],
  incomeSubCategories: []
};

const defaultMessages = {
  defaultErrorMessage: "Something went wrong, please try again!",
};

const CategoryReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'GET_INCOME_MAIN_CATEGORIES_SUCCESS':
      return Object.assign({}, state, {
        incomeMainCategories: action.mainCategories
      });
    case 'GET_INCOME_MAIN_CATEGORIES_ERROR':
      key = "defaultErrorMessage";
      addMessage(action.messages, createMessage(key, false, defaultMessages));
      return state;
    case 'INCOME_MAIN_CATEGORIES_ARE_READY':
      return {
        ...state,
        incomeMainCategoriesAreLoaded: true
      };

    case 'GET_INCOME_SUB_CATEGORIES_SUCCESS':
      return Object.assign({}, state, {
        incomeSubCategories: action.subCategories
      });
    case 'GET_INCOME_SUB_CATEGORIES_ERROR':
      key = "defaultErrorMessage";
      addMessage(action.messages, createMessage(key, false, defaultMessages));
      return state;
    case 'INCOME_SUB_CATEGORIES_ARE_READY':
      return {
        ...state,
        incomeSubCategoriesAreLoaded: true
      };
    default:
      return state;
  }
};

export default CategoryReducer;
