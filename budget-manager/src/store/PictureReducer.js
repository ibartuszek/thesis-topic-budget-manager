import {addMessage, createMessage} from "../actions/message/messageActions";
import {addElementToArray} from "../actions/common/listActions";

const initState = {
  pictures: []
};

const PictureReducer = (state = initState, action) => {
  let key;
  switch (action.type) {
    case 'UPLOAD_PICTURE_SUCCESS':
      key = "uploadPictureSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        pictures: addElementToArray(state.pictures, action.picture)
      });
    case 'UPLOAD_PICTURE_ERROR':
      key = "uploadPictureError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'GET_PICTURE_SUCCESS':
      key = "getPictureSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        pictures: addElementToArray(state.pictures, action.picture),
      });
    case 'GET_PICTURE_ERROR':
      key = "getPictureError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    case 'DELETE_PICTURE_SUCCESS':
      key = "deletePictureSuccess";
      addMessage(action.messages, createMessage(key, action.message, true));
      return Object.assign({}, state, {
        ...state,
        pictures: addElementToArray(state.pictures, action.picture),
      });
    case 'DELETE_PICTURE_ERROR':
      key = "deletePictureError";
      addMessage(action.messages, createMessage(key, action.message, false));
      return Object.assign({}, state, {
        ...state
      });
    default:
      return state;
  }
};

export default PictureReducer;
