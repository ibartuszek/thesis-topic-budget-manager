import {combineReducers} from 'redux';
import CategoryReducer from './CategoryReducer';
import LogReducer from './LogReducer';
import PictureReducer from "./PictureReducer";
import TransactionReducer from "./TransactionReducer";
import UserReducer from './UserReducer';
import StatisticsReducer from "./StatisticsReducer";

const RootReducer = combineReducers({
  categoryHolder: CategoryReducer,
  logHolder: LogReducer,
  pictureHolder: PictureReducer,
  statisticsHolder: StatisticsReducer,
  transactionHolder: TransactionReducer,
  userHolder: UserReducer,
});

export default RootReducer;
