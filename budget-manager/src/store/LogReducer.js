const initState = {
  messages: []
};

const LogReducer = (state = initState, action) => {
  switch (action.type) {
    case 'ADD_MESSAGE':
    case 'REMOVE_MESSAGE':
      return {
        ...state,
        messages: action.messages
      };
    default:
      return state;
  }
};

export default LogReducer;
