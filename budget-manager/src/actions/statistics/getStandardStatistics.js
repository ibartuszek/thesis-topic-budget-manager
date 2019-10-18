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
        type: "MONTHLY_BUDGET",
        currency: "EUR",
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
        totalIncomesLabel: 'Total incomes',
        totalExpenses: 1550.00,
        totalExpensesLabel: 'Total expenses',
        savings: 600,
        savingsLabel: 'Savings',
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
    customStatisticsIds: [
      {
        schemaId: 2,
        schemaTitle: "Example scale",

      },
      {
        schemaId: 3,
        schemaTitle: "Example sum"
      }
    ]
  };
  return {
    "status": responseStatus,
    "body": responseBody
  }
}
