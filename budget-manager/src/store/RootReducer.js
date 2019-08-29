import {combineReducers} from 'redux';
import UserReducer from './UserReducer';
import CategoryReducer from './CategoryReducer';

const RootReducer = combineReducers({
  userHolder: UserReducer,
  categoryHolder: CategoryReducer
});

export default RootReducer;
