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
    standardStatistics: {
      schema: {
        id: 1,
        title: "Monthly budget",
        types: "MONTHLY_BUDGET",
        currency: "EUR",
        monthly: true,
        chartType: "RADIAL",
      },
      startDate: "2019-10-01",
      endDate: "2019-10-31",
      chartData: {
        legend: "Expenses",
        sectorPoints: [
          {angle: 820.00, label: "Lodging"},
          {angle: 80, label: "Car"},
          {angle: 350, label: "Food"},
          {angle: 80, label: "Beauty care"},
          {angle: 100, label: "Health care"},
          {angle: 100, label: "Entertainment"},
          {angle: 600, label: 'Saving'}
        ]
      },
      budgetDetails: {
        totalIncomes: 2150.00,
        totalExpenses: 1550.00,
        savings: 600,
        incomes: [
          {mainCategoryName: "Salary", subCategoryName: null, amount: 2000.00},
          {mainCategoryName: "Salary", subCategoryName: "Allowance", amount: 100.00},
          {mainCategoryName: "Gift", subCategoryName: null, amount: 50.00}
        ],
        outcomes: [
          {mainCategoryName: "Lodging", subCategoryName: null, amount: 820.00},
          {mainCategoryName: "Lodging", subCategoryName: "Internet", amount: 20.00},
          {mainCategoryName: "Car", subCategoryName: null, amount: 80.00},
          {mainCategoryName: "Car", subCategoryName: "Petrol", amount: 80.00},
          {mainCategoryName: "Food", subCategoryName: null, amount: 350.00},
          {mainCategoryName: "Beauty care", subCategoryName: null, amount: 80.00},
          {mainCategoryName: "Health care", subCategoryName: null, amount: 100.00},
          {mainCategoryName: "Entertainment", subCategoryName: null, amount: 100.00},
          {mainCategoryName: "Entertainment", subCategoryName: "Cinema", amount: 20.00},
        ]
      }
    },
    monthlyCustomStatisticsIds: [
      {
        schemaId: 1,
        schemaTitle: "Example monthly custom statistics 1",

      },
      {
        schemaId: 2,
        schemaTitle: "Example monthly custom statistics 2"
      }
    ],
    irregularStatisticsIds: [
      {
        schemaId: 1,
        schemaTitle: "Example irregular statistics 1",

      },
      {
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

export function createGetCustomStatisticsMock() {
  return {
    schema: {
      id: 2,
      title: "Monthly scale",
      types: "CUSTOM_BUDGET",
      currency: "EUR",
      monthly: true,
      chartType: "BAR",
    },
    startDate: "2019-10-01",
    endDate: "2019-10-31",
    chartData: {
      legend: "Monthly-scale",
      dataPoints: [
        {x: 1, y: -5.00, label: 'I am legend'},
        {x: 2, y: 1995.00, label: 'Salary'},
        {x: 2, y: 2025.00, label: 'Fringe benefit'},
        {x: 6, y: 2125.00, label: 'Birthday present from grandparents'},
        {x: 6, y: 2079.55, label: 'Big shopping'},
        {x: 11, y: 2076.55, label: 'Found money'},
        {x: 10, y: 1985.64, label: 'Tax'},
        {x: 10, y: 1955.34, label: 'Common cost'},
        {x: 10, y: 1155.34, label: 'Lodging'},
        {x: 15, y: 1135.34, label: 'Internet'},
        {x: 21, y: 1132.34, label: 'C-vitamin'},
        {x: 21, y: 1052.00, label: 'Big shopping'},
        {x: 21, y: 1037.00, label: 'Online course'},
        {x: 21, y: 1017.00, label: 'New convenient keyboard'},
        {x: 26, y: 937.00, label: 'Petrol'},
        {x: 26, y: 902.00, label: 'Shopping'},
        {x: 30, y: 866.00, label: 'New shirt'},
      ]
    }
  };
}
