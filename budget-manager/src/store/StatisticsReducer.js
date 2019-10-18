import {addMessage, createMessage} from "../actions/message/messageActions";
import {addElementToArray} from "../actions/common/listActions";

const initState = {
  standardStatistics: null,
  standardStatisticsAreLoaded: undefined,
  customStatisticsIds: [],
  customStatistics: [],
  customStatisticsAreLoaded: undefined
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
        customStatisticsIds: action['response'].customStatisticsIds,
        standardStatisticsAreLoaded: true
      });
    case 'GET_STANDARD_STATISTICS_ERROR':
      key = "getStandardStatisticsError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'GET_CUSTOM_STATISTICS_SUCCESS':
      key = "getCustomStatisticsSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      console.log(action['response'].customStatistics);
      return Object.assign({}, state, {
        ...state,
        // TODO:
        customStatistics: addElementToArray(state.customStatistics, action['response'].customStatistics),
        customStatisticsAreLoaded: true,
      });
    case 'GET_CUSTOM_STATISTICS_ERROR':
      key = "getCustomStatisticsError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    default:
      return state;
  }
};

export default StatisticsReducer;
