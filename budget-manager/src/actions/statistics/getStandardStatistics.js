import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createDispatchContext} from "../common/createDispatchContext";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function getStandardStatistics(context, startDate, endDate) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let url = `/bm/statistics/getStandardStatistics?userId=` + userId
    + `&startDate=` + startDate + `&endDate=` + endDate;
  let successCase = 'GET_STANDARD_STATISTICS_SUCCESS';
  let errorCase = 'GET_STANDARD_STATISTICS_ERROR';
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
    let response = createGetStandardStatisticsMockResponse();
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

export function createGetStandardStatisticsMockResponse() {
  let responseStatus = 200;
  let responseBody = {
    message: "OK",
    standardStatistics: [
      {
        schemaId: 1,
        schemaTitle: "Example income radial chart",
        type: ["INCOME"],
        startDate: "2019-10-01",
        endDate: "2019-10-31",
        currency: "EUR",
        monthly: true,
        chartType: "RADIAL",
        height: 300,
        width: 500,
        data: [
          {
            legend: "Incomes",
            data: [
              {angle: 120, label: "Salary"},
              {angle: 20, label: "Reward"},
              {angle: 65, label: "Gift"}
            ]
          }
        ],
      }, {
        schemaId: 2,
        schemaTitle: "Example outcome radial chart",
        type: ["OUTCOME"],
        startDate: "2019-10-01",
        endDate: "2019-10-31",
        currency: "EUR",
        monthly: true,
        chartType: "RADIAL",
        height: 300,
        width: 500,
        data: [
          {
            legend: "Expenses",
            data: [
              {angle: 8, label: "Flat", labelsAboveChildren: true},
              {angle: 5, label: "Internet"},
              {angle: 4, label: "Food"}
            ]
          }
        ],
      }, {
        id: 3,
        schemaTitle: "Example income and outcome vertical bar chart",
        type: ["INCOME", "OUTCOME"],
        startDate: "2019-10-01",
        endDate: "2019-10-31",
        currency: "EUR",
        monthly: true,
        chartType: "LINEAR",
        height: 300,
        width: 500,
        data: [
          {
            legend: "test1",
            data: [
              {x: 0, y: 8},
              {x: 1, y: 5},
              {x: 2, y: 4},
              {x: 3, y: 9},
              {x: 4, y: 1},
              {x: 5, y: 7},
              {x: 6, y: 6},
              {x: 7, y: 3},
              {x: 8, y: 2},
              {x: 9, y: 0}
            ]
          }, {
            legend: "test2",
            data: [
              {x: 0, y: 18},
              {x: 1, y: 15},
              {x: 2, y: 14},
              {x: 3, y: 19},
              {x: 4, y: 11},
              {x: 5, y: 17},
              {x: 6, y: 16},
              {x: 7, y: 13},
              {x: 8, y: 12},
              {x: 9, y: 10}
            ]
          }
        ],
      }
    ],
    monthlyCustomStatisticsIds: [
      {
        schemaId: 1,
        schemaTitle: "Example monthly custom statistics 1",

      }, {
        schemaId: 2,
        schemaTitle: "Example monthly custom statistics 2"
      }
    ],
    irregularStatisticsIds: [
      {
        schemaId: 1,
        schemaTitle: "Example irregular statistics 1",

      }, {
        schemaId: 2,
        schemaTitle: "Example irregular statistics 2"
      }
    ]
  };
  return {
    "status": responseStatus,
    "body": responseBody
  }
}
