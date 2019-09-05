import {combineReducers} from 'redux';
import UserReducer from './UserReducer';
import LogReducer from './LogReducer';
import CategoryReducer from './CategoryReducer';

const RootReducer = combineReducers({
  logHolder: LogReducer,
  userHolder: UserReducer,
  categoryHolder: CategoryReducer
});

export default RootReducer;
