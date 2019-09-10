import {addMessage, createMessage} from "../actions/message/messageActions";
import {categoryMessages} from "./MessageHolder"
import {addElementToArray, replaceElementAtArray} from "../actions/common/listActions";

const initState = {
  incomeMainCategoriesAreLoaded: false,
  incomeSubCategoriesAreLoaded: false,
  incomeMainCategories: [],
  incomeSubCategories: [],
};

const CategoryReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'CREATE_INCOME_MAIN_CATEGORY_SUCCESS':
      key = "createMainCategorySuccess";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
        incomeMainCategories: addElementToArray(state.incomeMainCategories, action.mainCategoryModel),
      });
    case 'CREATE_INCOME_MAIN_CATEGORY_ERROR':
      key = "createMainCategoryError";
      addMessage(action.messages, createMessage(key, false, categoryMessages));
      return Object.assign({}, state, {
        ...state,
      });
    case 'CREATE_INCOME_SUB_CATEGORY_SUCCESS':
      key = "createSubCategorySuccess";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
        incomeSubCategories: addElementToArray(state.incomeSubCategories, action.subCategoryModel),
      });
    case 'CREATE_INCOME_SUB_CATEGORY_ERROR':
      key = "createSubCategoryError";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
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
      };
    case 'INCOME_MAIN_CATEGORIES_ARE_READY':
      return {
        ...state,
        incomeMainCategoriesAreLoaded: true,
      };
    case 'INCOME_SUB_CATEGORIES_ARE_READY':
      return {
        ...state,
        incomeSubCategoriesAreLoaded: true,
      };
    case 'UPDATE_INCOME_MAIN_CATEGORY_SUCCESS':
      key = "updateMainCategorySuccess";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
        incomeMainCategories: replaceElementAtArray(state.incomeMainCategories, action.mainCategoryModel),
      });
    case 'UPDATE_INCOME_MAIN_CATEGORY_ERROR':
      key = "updateMainCategoryError";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
      });
    case 'UPDATE_INCOME_SUB_CATEGORY_SUCCESS':
      key = "updateSubCategorySuccess";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
        incomeSubCategories: replaceElementAtArray(state.incomeSubCategories, action.subCategoryModel),
      });
    case 'UPDATE_INCOME_SUB_CATEGORY_ERROR':
      key = "updateSubCategoryError";
      addMessage(action.messages, createMessage(key, true, categoryMessages));
      return Object.assign({}, state, {
        ...state,
      });
    default:
      return state;
  }
};

export default CategoryReducer;
