import {addMessage, createMessage} from "../actions/message/messageActions";
import {addElementToArray, removeElementFromArray, replaceElementAtArray} from "../actions/common/listActions";

const initState = {
  standardStatistics: null,
  standardStatisticsAreLoaded: undefined,
  customStatistics: [],
  standardSchema: null,
  customSchemas: [],
  schemasAreLoaded: undefined
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
        customSchemas: action['response'].customSchemas,
        schemasAreLoaded: true
      });
    case 'GET_SCHEMAS_ERROR':
      key = "getSchemasError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'CREATE_SCHEMA_SUCCESS':
      key = "createSchemaSuccess";
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
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        customSchemas: replaceElementAtArray(state.customSchemas, action['schemaModel']),
      });
    case 'UPDATE_SCHEMA_ERROR':
      key = "updateSchemaError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'DELETE_SCHEMA_SUCCESS':
      key = "deleteSchemaSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        customSchemas: removeElementFromArray(state.customSchemas, action['schemaModel']),
      });
    case 'DELETE_SCHEMA_ERROR':
      key = "deleteSchemaError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });

    case 'GET_STANDARD_STATISTICS_SUCCESS':
      key = "getStandardStatisticsSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        standardStatistics: action['response'].statistics,
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
      return Object.assign({}, state, {
        ...state,
        customStatistics: addElementToArray(state.customStatistics, action['response'].statistics),
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
