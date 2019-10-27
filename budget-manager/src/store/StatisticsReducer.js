import {addMessage, createMessage} from "../actions/message/messageActions";
import {addElementToArray, replaceElementAtArray} from "../actions/common/listActions";

const initState = {
  standardStatistics: null,
  standardStatisticsAreLoaded: undefined,
  customStatistics: [],
  standardSchema: null,
  customSchemas: [],
};

const StatisticsReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'GET_SCHEMAS_SUCCESS':
      key = "getSchemasSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        standardSchema: action['response'].standardSchema,
        customSchemas: action['response'].customSchemas
      });
    case 'GET_SCHEMAS_ERROR':
      key = "getSchemasError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'CREATE_SCHEMA_SUCCESS':
      key = "createSchemaSuccess";
      console.log(action['schemaModel']);
      console.log(state.customSchemas);
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        customSchemas: addElementToArray(state.customSchemas, action['schemaModel']),
      });
    case 'CREATE_SCHEMA_ERROR':
      key = "createSchemaError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'UPDATE_SCHEMA_SUCCESS':
      key = "updateSchemaSuccess";
      console.log(action['schemaModel']);
      console.log(state.customSchemas);
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        customSchemas: replaceElementAtArray(state.customSchemas, action['schemaModel']),
      });
    case 'UPDATE_SCHEMA_SUCCESS':
      key = "updateSchemaError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });

    case 'GET_STANDARD_STATISTICS_SUCCESS':
      key = "getStandardStatisticsSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        standardStatistics: action['response'].standardStatistics,
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
        customStatistics: addElementToArray(state.customStatistics, action['response'].customStatistics),
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
