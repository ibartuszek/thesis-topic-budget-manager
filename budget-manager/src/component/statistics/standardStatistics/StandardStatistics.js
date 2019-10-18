import React, {Component} from 'react';
import CustomRadialChart from "./CustomRadialChart";

class StandardStatistics extends Component {

  chartDetails = {
    height: 350,
    width: 400,
  };

  render() {
    const {chartData, endDate, schema, startDate} = this.props.standardStatistics;
    const {
      incomes, outcomes, savings, savingsLabel, totalExpenses, totalExpensesLabel, totalIncomes, totalIncomesLabel
    } = this.props.standardStatistics.budgetDetails;

    let incomeList = this.createList(incomes, schema.currency);
    let outcomeList = this.createList(outcomes, schema.currency);

    return (
      <div className="card card-body custom-chart-details-container my-3 mx-auto">
        <div className="clearfix my-3 mx-auto container">
          <h3 className="mx-auto border-bottom border-primary border-2">Main statistics</h3>
          <div className="row">
            <div className="col-12 col-lg-6 my-3 text-center">
              <CustomRadialChart chartData={chartData} chartDetails={this.chartDetails}
                                 schema={schema}/>
            </div>
            <div className="col-12 col-lg-6 my-3">
              <h4>Details</h4>
              <div className="border-2 border-left border-success mb-2">
                <ul className="list-group list-group-flush">
                  {incomeList}
                </ul>
              </div>
              <div className="border-2 border-left border-danger mb-2">
                <ul className="list-group list-group-flush">
                  {outcomeList}
                </ul>
              </div>
            </div>
          </div>
          <div>
            <h4 className="d-inline-block">Sum</h4>
            <div className="d-inline-block float-right badge-pill badge-info mt-1">{"Interval: [" + startDate + ", " + endDate + "]"}</div>
            <div className="list-group">
              <div className="list-group-item-primary my-2 p-2 border border-primary border-2 rounded">
                <div className="float-left d-inline-block">{totalIncomesLabel}</div>
                <div className="float-right d-inline-block"><strong>{totalIncomes + " " + schema.currency}</strong></div>
              </div>
              <div className="list-group-item-warning my-2 p-2 border border-warning border-2 rounded">
                <div className="float-left d-inline-block">{totalExpensesLabel}</div>
                <div className="float-right d-inline-block"><strong>{totalExpenses + " " + schema.currency}</strong></div>
              </div>
              <div className="list-group-item-success my-2 p-2 border border-success border-2 rounded">
                <div className="float-left d-inline-block">{savingsLabel}</div>
                <div className="float-right d-inline-block"><strong>{savings + " " + schema.currency}</strong></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  createList(transactions, currency) {
    let transactionList = null;
    if (transactions !== null) {
      transactionList = transactions.map((transaction, i) =>
        <li className="list-group-item" key={i}>
          <div className="float-left d-inline-block">{transaction.subCategoryName !== null ? <small>{"  - " + transaction.subCategoryName}</small>
            : transaction.mainCategoryName}</div>
          <div className="float-right d-inline-block">{transaction.subCategoryName !== null ? <small>{transaction.amount + " " + currency}</small>
            : transaction.amount + " " + currency}</div>
        </li>);
    }
    return transactionList;
  }
}

export default StandardStatistics;
