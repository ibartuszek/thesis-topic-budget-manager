export function setAccessToken(jwtToken) {
  return function (dispatch) {
    console.log("GET_ACCESS_TOKEN_SUCCESS");
    dispatch({type: 'GET_ACCESS_TOKEN_SUCCESS', jwtToken: jwtToken});
  }
}
