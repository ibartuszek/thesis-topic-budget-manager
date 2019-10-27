import {createHeaderWithJwtAndJsonBody} from "../common/createHeader";
import {createDispatchContext} from "../common/createDispatchContext";
import {dispatchError, dispatchSuccess} from "../common/dispatchActions";
import {defaultMessages} from "../../store/MessageHolder";

export function getCustomStatistics(context, startDate, endDate, schemaId) {
  const {userId, jwtToken, messages} = context;
  let header = createHeaderWithJwtAndJsonBody(jwtToken);
  let url = `/bm/statistics/getCustomStatistics?userId=` + userId
    + `&startDate=` + startDate + `&endDate=` + endDate + `schemaId=` + schemaId;
  let successCase = 'GET_CUSTOM_STATISTICS_SUCCESS';
  let errorCase = 'GET_CUSTOM_STATISTICS_ERROR';
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
    let response = createGetCustomStatisticsMockResponse(schemaId);
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

export function createGetCustomStatisticsMockResponse(schemaId) {
  let responseStatus = 200;
  console.log(schemaId);
  let responseBody = schemaId === 2 ? createGetCustomScaleStatisticsMock()
    : createGetCustomSumStatisticsMock();
  return {
    "status": responseStatus,
    "body": responseBody
  }
}

export function createGetCustomScaleStatisticsMock() {
  return {
    message: "OK",
    customStatistics: {
      schema: {
        id: 2,
        title: "Example scale",
        type: "CUSTOM_SCALE",
        currency: "EUR",
        chartType: "BAR",
      },
      startDate: "2019-10-01",
      endDate: "2019-10-31",
      chartData: {
        legend: "Monthly-scale",
        dataPoints: [
          {x: 0, y: 5.00, label: 'I am legend', date: '2019-10-01'},
          {x: 1, y: 1995.00, label: 'Salary', date: '2019-10-02'},
          {x: 2, y: 2025.00, label: 'Fringe benefit', date: '2019-10-02'},
          {x: 3, y: 2125.00, label: 'Birthday present from grandparents', date: '2019-10-06'},
          {x: 4, y: 2079.55, label: 'Big shopping', date: '2019-10-06'},
          {x: 5, y: 1985.64, label: 'Tax', date: '2019-10-10'},
          {x: 6, y: 1955.34, label: 'Common cost', date: '2019-10-10'},
          {x: 7, y: 1155.34, label: 'Lodging', date: '2019-10-10'},
          {x: 8, y: 1160.34, label: 'Found money', date: '2019-10-11'},
          {x: 9, y: 1135.34, label: 'Internet', date: '2019-10-15'},
          {x: 10, y: 1132.34, label: 'C-vitamin', date: '2019-10-21'},
          {x: 11, y: 1052.00, label: 'Big shopping', date: '2019-10-21'},
          {x: 12, y: 1037.00, label: 'Online course', date: '2019-10-21'},
          {x: 13, y: 1017.00, label: 'New convenient keyboard', date: '2019-10-21'},
          {x: 14, y: 937.00, label: 'Petrol', date: '2019-10-26'},
          {x: 15, y: 902.00, label: 'Shopping', date: '2019-10-26'},
          {x: 16, y: 866.00, label: 'New shirt', date: '2019-10-30'},
        ]
      },
      budgetDetails: {
        totalIncomes: {
          amount: 2150.00,
          label: 'Total incomes'
        },
        totalExpenses: {
          amount: 1550.00,
          label: 'Total expenses',
        },
        savings: {
          amount: 600,
          label: 'Savings'
        }
      }
    }
  };
}

export function createGetCustomSumStatisticsMock() {
  return {
    message: "OK",
    customStatistics: {
      schema: {
        id: 3,
        title: "Example sum",
        type: "CUSTOM_SUM",
        currency: "EUR",
        chartType: "LINEAR",
        mainCategoryName: 'Entertainment',
        subCategoryName: null,
      },
      startDate: "2019-10-01",
      endDate: "2019-10-31",
      chartData: {
        legend: "Monthly-scale",
        dataPoints: [
          {x: 0, y: 5.00, label: 'Aladin', date: '2019-08-14'},
          {x: 1, y: 95.00, label: 'New video card', date: '2019-08-19'},
          {x: 2, y: 100.00, label: 'Bad boys', date: '2019-09-13'},
          {x: 3, y: 120.00, label: 'New convenient keyboard', date: '2019-09-23'},
          {x: 4, y: 125.00, label: 'I am legend', date: '2019-10-03'},
        ]
      },
      budgetDetails: {
        totalExpenses: {
          amount: 125.00,
          label: 'Total cost of Entertainment'
        }
      }
    }
  };
}
