import React, {Component} from 'react';
import CustomChart from "./CustomChart";

class CustomStatistics extends Component {

  chartDetails = {
    height: 350,
    width: 400,
  };

  render() {
    const {chartData, endDate, schema, startDate} = this.props.customStatistics;
    const {savings, savingsLabel, totalExpenses, totalExpensesLabel, totalIncomes, totalIncomesLabel} = this.props.customStatistics.budgetDetails;

    let totalIncomesContainer = totalIncomes === null ? null :
      (<div className="list-group-item my-2 p-2 border border-primary border-2 rounded">
        <div className="float-left d-inline-block">{totalIncomesLabel}</div>
        <div className="float-right d-inline-block"><strong>{totalIncomes + " " + schema.currency}</strong></div>
      </div>);

    let totalExpensesContainer = totalExpenses === null ? null :
      (<div className="list-group-item my-2 p-2 border border-warning border-2 rounded">
        <div className="float-left d-inline-block">{totalExpensesLabel}</div>
        <div className="float-right d-inline-block"><strong>{totalExpenses + " " + schema.currency}</strong></div>
      </div>);

    let totalSavingsContainer = savings === null ? null :
      (<div className="list-group-item my-2 p-2 border border-success border-2 rounded">
        <div className="float-left d-inline-block">{savingsLabel}</div>
        <div className="float-right d-inline-block"><strong>{savings + " " + schema.currency}</strong></div>
      </div>);

    return (
      <div className="card card-body custom-chart-details-container my-3 mx-auto">
        <div className="clearfix my-3 mx-auto container">
          <h3 className="mx-auto border-bottom border-primary border-2">{schema.title}</h3>
          <div className="row">
            <div className="col-12 col-lg-6 my-3 text-center">
              <CustomChart chartData={chartData} chartDetails={this.chartDetails}
                           schema={schema}/>
            </div>
            <div className="col-12 col-lg-6 my-3">
              <h4 className="d-inline-block">Sum</h4>
              <div className="d-inline-block float-right badge-pill badge-info mt-1">{"Interval: [" + startDate + ", " + endDate + "]"}</div>
              <div className="list-group">
                {totalIncomesContainer}
                {totalExpensesContainer}
                {totalSavingsContainer}
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

}

export default CustomStatistics;
