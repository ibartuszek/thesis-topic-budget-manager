import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createDispatchContext} from "../common/createDispatchContext";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";
import {transformSchemaFromResponse, transformSchemaListFromResponse} from "./createSchemaMethods";

export function fetchSchemas(context) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let url = `/bm/statistics/schema/findAll?userId=` + userId;
  let successCase = 'GET_SCHEMAS_SUCCESS';
  let errorCase = 'GET_SCHEMAS_ERROR';
  let responseStatus = null;

  return function (dispatch) {
    let dispatchContext = createDispatchContext(dispatch, messages, successCase, errorCase);
    // return fetch(url, {
    //   method: 'GET',
    //   headers: header,
    // }).then(function (response) {
    //     responseStatus = response.status;
    //     return responseStatus === 200 ? response.json() : createErrorBody(response);
    //   }
    // ).then(function (responseBody) {
    let response = createGetStatisticsSchemasMockResponse();
    responseStatus = response.status;
    let responseBody = response.body;
    if (responseStatus === 200) {
      let response = createTransformedResponse(responseBody);
      return dispatchSuccess(dispatchContext, responseBody['message'], 'response', response);
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

function createTransformedResponse(responseBody) {
  let response = {};
  response.standardSchema = transformSchemaFromResponse(responseBody.standardSchema);
  response.customSchemas = transformSchemaListFromResponse(responseBody.customSchemas);
  return response;
}

export function createGetStatisticsSchemasMockResponse() {
  let responseStatus = 200;
  let responseBody = {
    message: "OK",
    standardSchema: {
      id: 1,
      title: "Monthly budget",
      type: "STANDARD",
      currency: "EUR",
      chartType: "RADIAL"
    },
    customSchemas: [
      {
        id: 2,
        title: "Example scale",
        type: "SCALE",
        currency: "EUR",
        chartType: "BAR"
      },
      {
        id: 3,
        title: "Example sum",
        type: "SUM",
        currency: "EUR",
        chartType: "LINEAR",
        mainCategory: {
          id: 10,
          name: "Entertainment",
          transactionType: "OUTCOME",
          userId: 1
        },
        subCategory: null
      }
    ]
  };
  return {
    "status": responseStatus,
    "body": responseBody
  }
}
