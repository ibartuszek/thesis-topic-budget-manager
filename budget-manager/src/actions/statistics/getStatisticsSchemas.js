import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createDispatchContext} from "../common/createDispatchContext";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function getStatisticsSchemas(context) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let url = `/bm/statistics/getStatisticsSchemas?userId=` + userId;
  let successCase = 'GET_STATISTICS_SCHEMAS_SUCCESS';
  let errorCase = 'GET_STATISTICS_SCHEMAS_ERROR';
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
      return dispatchSuccess(dispatchContext, responseBody['message'], 'response', responseBody);
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

export function createGetStatisticsSchemasMockResponse() {
  let responseStatus = 200;
  let responseBody = {
    message: "OK",
    standardStatisticsSchemas: {
      id: 1,
      title: "Monthly budget",
      type: "MONTHLY_BUDGET",
      currency: "EUR",
      chartType: "RADIAL"
    },
    customStatisticsSchemas: [
      {
        id: 2,
        title: "Example scale",
        type: "CUSTOM_SCALE",
        currency: "EUR",
        chartType: "BAR"
      },
      {
        id: 3,
        title: "Example sum",
        type: "CUSTOM_SUM",
        currency: "EUR",
        chartType: "LINEAR",
        mainCategoryName: 'Entertainment',
        subCategoryName: null
      }
    ]
  };
  return {
    "status": responseStatus,
    "body": responseBody
  }
}
