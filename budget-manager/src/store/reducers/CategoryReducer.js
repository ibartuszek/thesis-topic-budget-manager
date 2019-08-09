const initState = {
  incomeMainCategoriesAreLoaded: false,
  incomeSubCategoriesAreLoaded: false,
  incomeMainCategories: [],
  incomeSubCategories: []
};

const CategoryReducer = (state = initState, action) => {
  switch (action.type) {
    case 'GET_INCOME_MAIN_CATEGORIES_SUCCESS':
      console.log('get income main categories from server: success');
      return Object.assign({}, state, {
        incomeMainCategories: action.incomeMainCategories
      });
    case 'GET_INCOME_MAIN_CATEGORIES_ERROR':
      console.log('get income main categories from server: error');
      return state;
    case 'INCOME_MAIN_CATEGORIES_ARE_READY':
      console.log('income main categories are ready to use.');
      return {
        ...state,
        mainCategoriesAreLoaded: true
      };
    default:
      return state;
  }
};

export default CategoryReducer;
