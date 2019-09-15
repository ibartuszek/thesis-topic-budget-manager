import {combineReducers} from 'redux';
import CategoryReducer from './CategoryReducer';
import LogReducer from './LogReducer';
import TransactionReducer from "./TransactionReducer";
import UserReducer from './UserReducer';

const RootReducer = combineReducers({
  categoryHolder: CategoryReducer,
  logHolder: LogReducer,
  transactionHolder: TransactionReducer,
  userHolder: UserReducer,
});

export default RootReducer;
