import {combineReducers} from 'redux';
import CategoryReducer from './CategoryReducer';

const RootReducer = combineReducers({
    categoryHolder: CategoryReducer
});

export default RootReducer;
