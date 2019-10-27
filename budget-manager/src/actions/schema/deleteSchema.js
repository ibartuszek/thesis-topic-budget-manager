import {createDispatchContext} from "../common/createDispatchContext";
import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";
import {transformSchemaFromResponse, transformSchemaToRequest} from "./createSchemaMethods";

export function deleteSchema(context, schema) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let body = JSON.stringify(createBody(schema, userId));
  let successCase = 'DELETE_SCHEMA_SUCCESS';
  let errorCase = 'DELETE_SCHEMA_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    // return fetch(`/bm/statistics/schema/delete`, {
    //   method: 'DELETE',
    //   headers: header,
    //   body: body
    // }).then(function (response) {
    //     responseStatus = response.status;
    //     return responseStatus === 200 ? response.json() : createErrorBody(response);
    //   }
    // ).then(function (responseBody) {
    let response = createSchemaMock(schema);
    responseStatus = response.status;
    let responseBody = response.body;
    if (responseStatus === 200) {
      let schemaModel = transformSchemaFromResponse(responseBody['schema']);
      return dispatchSuccess(dispatchContext, responseBody['message'], 'schemaModel', schemaModel);
    } else if (responseStatus === 400 || responseStatus === 409 || responseStatus === 404) {
      return dispatchError(dispatchContext, responseBody);
    } else {
      return dispatchError(dispatchContext, defaultMessages['defaultErrorMessage']);
    }
    // }).catch(errorMessage => {
    //   console.log(errorMessage);
    // });
  }
}

function createBody(schemaModel, userId) {
  let body = {};
  body.userId = userId;
  body.schema = transformSchemaToRequest(schemaModel);
  return body;
}

function createSchemaMock(schema) {
  let responseStatus = 200;
  let schemaFromResponse = transformSchemaToRequest(schema);
  let responseBody = {
    message: "Schema has been deleted",
    schema: schemaFromResponse
  };
  return {
    "status": responseStatus,
    "body": responseBody
  }
}
