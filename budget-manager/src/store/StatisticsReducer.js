import {addMessage, createMessage} from "../actions/message/messageActions";
import {addElementToArray} from "../actions/common/listActions";

const initState = {
  standardStatistics: [],
  standardStatisticsAreLoaded: undefined,
  monthlyCustomStatisticsIds: [],
  monthlyCustomStatistics: [],
  irregularStatisticsIds: [],
  irregularStatistics: [],
};

const StatisticsReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'GET_STANDARD_STATISTICS_SUCCESS':
      key = "getStandardStatisticsSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        standardStatistics: action['response'].standardStatistics,
        monthlyCustomStatisticsIds: action['response'].monthlyCustomStatisticsIds,
        irregularStatisticsIds: action['response'].irregularStatisticsIds,
        standardStatisticsAreLoaded: true
      });
    case 'GET_STANDARD_STATISTICS_ERROR':
      key = "getStandardStatisticsError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'GET_MONTHLY_CUSTOM_STATISTICS_SUCCESS':
      key = "getMonthlyCustomStatisticsSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        // TODO:
        pictures: addElementToArray(state.standardStatistics, action.picture)
      });
    case 'GET_MONTHLY_CUSTOM_STATISTICS_ERROR':
      key = "getMonthlyCustomStatisticsError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'GET_IRREGULAR_STATISTICS_SUCCESS':
      key = "getIrregularStatisticsSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        // TODO:
        pictures: addElementToArray(state.standardStatistics, action.picture)
      });
    case 'GET_IRREGULAR_STATISTICS_ERROR':
      key = "getIrregularStatisticsError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    default:
      return state;
  }
};

export default StatisticsReducer;
