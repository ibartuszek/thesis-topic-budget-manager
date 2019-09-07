import {addMessage, createMessage} from "../actions/message/messageActions";
import {categoryMessages} from "./MessageHolder"
import {addElementToArray} from "../actions/common/listActions";

const initState = {
  incomeMainCategoriesAreLoaded: false,
  incomeSubCategoriesAreLoaded: false,
  incomeMainCategories: [],
  incomeSubCategories: [],
  messages: {
    createMainCategorySuccess: null,
    createMainCategoryError: null,
    incomeCategoriesUnavailableMessage: null
  }
};

const CategoryReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'CREATE_MAIN_INCOME_CATEGORY_SUCCESS':
      key = "createMainCategorySuccess";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
        incomeMainCategories: addElementToArray(state.incomeMainCategories, action.mainCategoryModel),
        messages: {
          ...state.messages,
          createMainCategorySuccess: categoryMessages[key],
          createMainCategoryError: null,
        }
      });
    case 'CREATE_MAIN_INCOME_CATEGORY_ERROR':
      key = "createMainCategoryError";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
        messages: {
          ...state.messages,
          createMainCategorySuccess: null,
          createMainCategoryError: categoryMessages[key],
        }
      });
    case 'GET_INCOME_MAIN_CATEGORIES_SUCCESS':
      return Object.assign({}, state, {
        ...state,
        incomeMainCategories: action.mainCategories,
      });
    case 'GET_INCOME_MAIN_CATEGORIES_ERROR':
      key = "categoriesUnavailableMessage";
      addMessage(action.messages, createMessage(key, false, categoryMessages));
      return {
        ...state,
        messages: {
          ...state.messages,
          incomeCategoriesUnavailableMessage: categoryMessages[key]
        }
      };
    case 'INCOME_MAIN_CATEGORIES_ARE_READY':
      return {
        ...state,
        incomeMainCategoriesAreLoaded: true,
        messages: {
          ...state.messages,
          incomeCategoriesUnavailableMessage: null
        }
      };

    case 'GET_INCOME_SUB_CATEGORIES_SUCCESS':
      return Object.assign({}, state, {
        ...state,
        incomeSubCategories: action.subCategories
      });
    case 'GET_INCOME_SUB_CATEGORIES_ERROR':
      key = "categoriesUnavailableMessage";
      addMessage(action.messages, createMessage(key, false, categoryMessages));
      return {
        ...state,
        messages: {
          ...state.messages,
          incomeCategoriesUnavailableMessage: categoryMessages[key]
        }
      };
    case 'INCOME_SUB_CATEGORIES_ARE_READY':
      return {
        ...state,
        incomeSubCategoriesAreLoaded: true,
        messages: {
          ...state.messages,
          incomeCategoriesUnavailableMessage: null
        }
      };
    default:
      return state;
  }
};

export default CategoryReducer;
