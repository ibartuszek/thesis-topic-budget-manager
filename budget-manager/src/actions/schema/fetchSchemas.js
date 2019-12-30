import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createDispatchContext} from "../common/createDispatchContext";
import {createErrorBody} from "../common/createErrorBody";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";
import {transformSchemaFromResponse, transformSchemaListFromResponse} from "./createSchemaMethods";

export function fetchSchemas(context) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let url = process.env.REACT_APP_API_ENDPOINT + `/bm/statistics/schema/findAll?userId=` + userId;
  let successCase = 'GET_SCHEMAS_SUCCESS';
  let errorCase = 'GET_SCHEMAS_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    return fetch(url, {
      method: 'GET',
      headers: header,
    }).then(function (response) {
        responseStatus = response.status;
        return responseStatus === 200 ? response.json() : createErrorBody(response);
      }
    ).then(function (responseBody) {
      if (responseStatus === 200) {
        let response = createTransformedResponse(responseBody);
        return dispatchSuccess(dispatchContext, responseBody['message'], 'response', response);
      } else if (responseStatus === 400 || responseStatus === 409 || responseStatus === 404) {
        return dispatchError(dispatchContext, responseBody);
      } else {
        return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
      }
    }).catch(errorMessage => {
      console.log(errorMessage);
    });
  }
}

function createTransformedResponse(responseBody) {
  let response = {};
  response.standardSchema = transformSchemaFromResponse(responseBody.standardSchema);
  response.customSchemas = transformSchemaListFromResponse(responseBody.customSchemas);
  return response;
}
